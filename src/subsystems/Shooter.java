package src.subsystems;

import lib.Commands;
import lib.Commands.Command;
import lib.Motor;
import src.Constants;

/**
 * Shooter subsystem.
 */
public class Shooter {
    // Single motor controls shooter feeder wheels
    private final Motor _shooterMotor = new Motor();

    /**
     * Creates a new Shooter subsystem.
     */
    public Shooter() {}

    /** 
     * Spins feeders and shoots. Spins at full power for Constants.shooterRunTimeSeconds, then stops.
     */
    public Command shoot() {
        // TODO
    }
}