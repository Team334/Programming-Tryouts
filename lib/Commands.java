package lib;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * A small, rough approximation of WPILib's upcoming "Commands v3" framework:
 * https://github.com/wpilibsuite/allwpilib/blob/main/design-docs/commands-v3.md
 *
 * The real v3 lets you write a whole command as one normal-looking, blocking-style method
 * instead of splitting it into initialize()/execute()/isFinished()/end() like the old
 * command framework. It does this with a Coroutine that the command periodically yields
 * control back to.
 *
 * This version gets the same *syntax* of code by giving every running command its own real
 * Java virtual thread. Subsystems actually run at the same time instead of one big loop faking it.
 *
 */
public final class Commands {
    private Commands() {}

    /** One unit of robot behavior. Build these with Commands.run(...). */
    public record Command(Consumer<Coroutine> body, Set<Object> requirements, String name) {

        /**
         * Declares which subsystems (or individual motors) this command needs exclusive
         * control of. Scheduling a command that requires something another running command
         * is also using interrupts that older command.
         */
        public Command requiring(Object... mechanisms) {
            return new Command(body, Set.of(mechanisms), name);
        }

        /** Gives the command a readable name, printed to the console as it runs. */
        public Command named(String newName) {
            return new Command(body, requirements, newName);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /** Handed to every running command's body so it can cooperate with everything else. */
    public static final class Coroutine {

        /** Call this inside any loop you write, so other commands get a chance to run. */
        public void yield() {
            if (Thread.interrupted()) {
                throw new Cancelled();
            }

            Thread.onSpinWait();
        }

        /** Pauses just this command for the given number of seconds. */
        public void sleep(double seconds) {
            try {
                Thread.sleep((long) (seconds * 1000));
            } catch (InterruptedException e) {
                throw new Cancelled();
            }
        }

        /** Runs another command and waits for it to finish before this one continues. */
        public void await(Command command) {
            join(schedule(command));
        }
    }

    /** Thrown internally when a command gets interrupted. Commands don't need to catch this. */
    private static final class Cancelled extends RuntimeException {
        private Cancelled() {
            super(null, null, false, false);
        }
    }

    /** Builds a command out of a body that runs asynchronously, given a Coroutine to work with. */
    public static Command run(Consumer<Coroutine> body) {
        return new Command(body, Set.of(), "Command");
    }

    /** Runs commands one after another, in order, waiting for each to finish before the next starts. */
    public static Command sequence(Command... steps) {
        return run(co -> {
            for (Command step : steps) {
                co.await(step);
            }
        }).named("Sequence");
    }

    // Which thread is currently allowed to use each requirement.
    private static final Map<Object, Thread> owners = new ConcurrentHashMap<>();

    // Which Command belongs to each running thread.
    private static final Map<Thread, Command> threadToCommand = new ConcurrentHashMap<>();


    /**
     * Returns the command currently using this mechanism.
     * Returns null if the mechanism is currently idle.
     */
    public static Command owner(Mechanism mechanism) {
        Thread thread = owners.get(mechanism);

        if (thread == null || !thread.isAlive()) {
            return null;
        }

        return threadToCommand.get(thread);
    }


    /** Starts a command on its own virtual thread and returns immediately. */
    public static Thread schedule(Command command) {

        for (Object req : command.requirements()) {
            Thread current = owners.get(req);

            if (current != null && current.isAlive()) {
                System.out.println(
                    "[Commands] " + command
                    + " is interrupting whatever was using " + req
                );

                current.interrupt();
            }
        }

        Thread thread = Thread.ofVirtual()
            .name(command.name())
            .unstarted(() -> {

                System.out.println("[Commands] starting: " + command);

                try {
                    command.body().accept(new Coroutine());

                    System.out.println("[Commands] finished: " + command);

                } catch (Cancelled e) {

                    System.out.println("[Commands] interrupted: " + command);

                } finally {

                    // Forget which command this thread was running.
                    threadToCommand.remove(Thread.currentThread());

                    // Give up ownership of all requirements.
                    for (Object req : command.requirements()) {
                        owners.remove(req, Thread.currentThread());
                    }
                }
            });


        // Remember which command this thread is running.
        threadToCommand.put(thread, command);


        // Give this thread ownership of the command's requirements.
        for (Object req : command.requirements()) {
            owners.put(req, thread);
        }


        thread.start();

        return thread;
    }


    /** Starts several commands at the same time and waits for ALL of them to finish. */
    public static void parallel(Command... commands) {
        Thread[] threads = new Thread[commands.length];

        for (int i = 0; i < commands.length; i++) {
            threads[i] = schedule(commands[i]);
        }

        for (Thread thread : threads) {
            join(thread);
        }
    }


    private static void join(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}