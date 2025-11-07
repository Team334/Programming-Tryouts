package src;

import src.subsystems.Drive;
import src.subsystems.Intake;
import src.subsystems.Shooter;

/**
 * Main robot class that coordinates all subsystems and executes the autonomous routine.
 */
public class Robot {
    private final Drive _drive = new Drive();
    private final Shooter _shooter = new Shooter();
    private final Intake _intake = new Intake();

    /**
     * Performs one "cycle" of the autonomous routine. This means driving to a game piece, intaking it,
     * driving to the scoring position, and then shooting it.
     */
    private void cycle() {
        // TODO
    }

    /**
     * The autonomous routine.
     */
    private void autonomous() {
        while (true) {
            cycle();
        }
    }

    public static void main(String[] args) {
        Robot robot = new Robot();
    
        robot.autonomous();
    }
}
