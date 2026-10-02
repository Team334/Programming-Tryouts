package src;

import src.mechanisms.Drive;
import src.mechanisms.Hopper;
import src.mechanisms.Intake;
import src.mechanisms.LED;
import src.mechanisms.Shooter;
import src.sim.Simulation;

/**
 * Main robot class that owns all subsystems.
 */
public class Robot {
    public final Drive drive = new Drive();
    public final Intake intake = new Intake();
    public final Hopper hopper = new Hopper();
    public final Shooter shooter = new Shooter();
    public final LED led = new LED(drive, intake, hopper, shooter);

    public static void main(String[] args) {
        Robot robot = new Robot();

        new Simulation(robot).run();
    }
}