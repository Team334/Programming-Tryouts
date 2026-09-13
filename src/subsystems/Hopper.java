package src.subsystems;

import lib.Commands;
import lib.Commands.Command;
import lib.Motor;
import src.Constants;

/**
 * Hopper subsystem.
 */
public class Hopper {
    // Two motors control feeder wheels on the left and right sides of the hopper
    private final Motor _leftMotor = new Motor();
    private final Motor _rightMotor = new Motor();

    /**
     * Creates a new Hopper subsystem.
     */
    public Hopper() {}

    /**
     * Spins both feeder motors at full power to index game pieces toward the shooter,
     * for Constants.hopperIndexTimeSeconds, then stops.
     */
    public Command index() {
        // TODO
    }
}