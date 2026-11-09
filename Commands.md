# `Commands` (`lib.Commands`)

## What this class is

`Commands` is a runnable that tells the robot what to do.

### For example:
"spin the intake for two seconds" Commands lets you write that as one normal, top-to-bottom method instead:

```java
Commands.run(co -> {
    intake.setPower(1.0);
    co.sleep(2.0);
    intake.setPower(0.0);
}).requiring(intake);
```

### `Command` (record)

A `Command` is a description of a unit of robot behavior. It does nothing on its own until it's handed to `Commands.schedule(...)`. It bundles three things:

| Field          | Type                  | Meaning                                                                                       |
| -------------- | --------------------- | --------------------------------------------------------------------------------------------- |
| `body`         | `Consumer<Coroutine>` | The actual code to run, as a lambda that takes a `Coroutine` and runs top to bottom.          |
| `requirements` | `Set<Object>`         | The subsystems/motors/etc. This command needs exclusive use of. Anything can be a requirement |
| `name`         | `String`              | Defaults to `"Command"`.                                                                      |

`Command` has two methods that return a **new** `Command` rather than mutating the original (records are immutable):

- **`.requiring(Object... mechanisms)`** — declares which requirements this command needs. Scheduling a command that requires something another currently-running command also requires **interrupts** that older command (see below).
- **`.named(String newName)`** — sets the name.

Both are meant to be chained onto `Commands.run(...)`:

```java
Commands.run(co -> { ... }).requiring(shooter).named("Spin Up Shooter");
```

### `Coroutine`

This is what a command's body uses to cooperate with everything else going on. A fresh one is created for each running command and passed into its body lambda. It has three methods:

- **`yield()`** — **Call this inside any hand-written loop** in a command body (e.g. `while (!atTarget) { ...; co.yield(); }`). It's a checkpoint: it checks whether this command's thread has been interrupted (cancelled), and if so, throws internally to unwind out of the command. If a loop never calls `yield()` that command **won't notice it's been cancelled** until it happens to hit one of these calls.
- **`sleep(double seconds)`** — Pauses _only this command's thread_ for the given duration and the rest of the robot keeps running.
- **`await(Command command)`** — Runs another command and blocks this one until it finishes.

### Cancellation (`Cancelled`)

A unchecked exception used purely as a control-flow signal meaning "this command's thread was interrupted, stop running now."

## Requirements and interruption

The `owners` map (`Map<Object, Thread>`) is the whole interruption mechanism: it tracks which thread currently "owns" each requirement object. When a command is scheduled:

1. For each of its requirements, check if some other _still-alive_ thread already owns it.
2. If so, print a console message and call `.interrupt()` on that thread. This is a **cooperative** interrupt and the old command's thread only actually stops the next time it hits a `yield()`, `sleep()`, or `await()` call, at which point `Cancelled` is thrown and that command's `finally` block runs.
3. Claim ownership of all of this command's requirements for the new thread _before_ starting it, so a command needing two things can't have another command sneak in and claim one of them mid-way.
4. When the command's thread finishes (normal completion or `Cancelled`), it `finally` block releases ownership of each requirement.

A command with no requirements (the default, `Set.of()`) never interrupts anything and is never interrupted by ownership conflicts.

## API reference

| Method                                   | Returns   | What it does                                                                                                                             |
| ---------------------------------------- | --------- | ---------------------------------------------------------------------------------------------------------------------------------------- |
| `Commands.run(Consumer<Coroutine> body)` | `Command` | Wraps a body into a `Command` with no requirements and the default name `"Command"`.                                                     |
| `Commands.sequence(Command... steps)`    | `Command` | Builds one `Command` that runs each step in order, `await`-ing each before starting the next.                                            |
| `Commands.schedule(Command command)`     | `Thread`  | Starts a command on its own virtual thread and returns **immediately**. Handles interrupting whatever previously owned its requirements. |
| `Commands.parallel(Command... commands)` | `void`    | Schedules all given commands at once, then **blocks the calling thread** until every one of them has finished (or been interrupted).     |
| `Commands.owner(Mechanism... mechanism)` | `Command` | Returns the command currently using this mechanism. Returns null if mechanism is currently idle.                                         |
