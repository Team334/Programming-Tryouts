package src.mechanisms;

import lib.Commands;
import lib.Mechanism;
import lib.Commands.Command;
import lib.Motor;
import src.Constants;

/**
 * Shooter subsystem.
 */
public class Shooter extends Mechanism {
    // Single motor controls shooter feeder wheels
    private final Motor _shooterMotor = new Motor();

    /**
     * Creates a new Shooter subsystem.
     */
    public Shooter() {
        super("Mechanism");
    }

    /**
     * Spins feeders and shoots. Spins at full power for Constants.shooterRunTimeSeconds, then stops.
     */
    public Command shoot() {
        // TODO: Run the shooter motor for Constants.shooterRunTimeSeconds using commands
    }
}