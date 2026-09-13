package src.mechanisms;

import lib.Commands;
import lib.Mechanism;
import lib.Commands.Command;
import lib.Motor;
import src.Constants;

/**
 * Hopper subsystem.
 */
public class Hopper extends Mechanism {
    // Two motors control feeder wheels on the left and right sides of the hopper
    private final Motor _leftMotor = new Motor();
    private final Motor _rightMotor = new Motor();

    /**
     * Creates a new Hopper subsystem.
     */
    public Hopper() {
        super("Hopper");
    }

    /**
     * Spins both feeder motors at full power to index game pieces toward the shooter,
     * for Constants.hopperIndexTimeSeconds, then stops.
     */
    public Command index() {
        // TODO: Run the intake motor for Constants.hopperIndexTimeSeconds using commands
    }
}
