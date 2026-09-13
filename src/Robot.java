package src;

import src.sim.Simulation;
import src.subsystems.Drive;
import src.subsystems.Hopper;
import src.subsystems.Intake;
import src.subsystems.Shooter;

/**
 * Main robot class that owns all subsystems.
 * 
 * There's no fixed autonomous routine anymore — main() instead hands control to the
 * keyboard-driven Simulation (src/sim/Simulation.java), which schedules commands on
 * these subsystems in response to key presses, the same way real teleop code schedules
 * commands in response to joystick buttons. Because each command runs on its own
 * virtual thread (see lib/Commands.java), driving, intaking, indexing, and shooting can
 * all genuinely happen at the same time — try holding a drive key and pressing I/H/SPACE.
 */
public class Robot {
    public final Drive drive = new Drive();
    public final Intake intake = new Intake();
    public final Hopper hopper = new Hopper();
    public final Shooter shooter = new Shooter();

    public static void main(String[] args) {
        Robot robot = new Robot();

        new Simulation(robot).run();
    }
}