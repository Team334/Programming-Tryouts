package src.mechanisms;

import lib.Commands;
import lib.Mechanism;
import lib.Commands.Command;
import lib.Motor;
import src.Constants;

/**
 * Intake subsystem.
 */
public class Intake extends Mechanism {
    // Single motor controls intake feeder wheels
    private final Motor _intakeMotor = new Motor();

    /**
     * Creates a new Intake subsystem.
     */
    public Intake() {
        super("Intake");
    }

    /**
     * Runs the intake mechanism to collect a game piece.
     * Spins at full power for Constants.intakeRunTimeSeconds, then stops.
     */
    public Command intake() {
        // TODO: Run the intake motor for Constants.intakeRunTimeSeconds using commands
    }
}