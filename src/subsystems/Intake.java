package src.subsystems;

import lib.Commands;
import lib.Commands.Command;
import lib.Motor;
import src.Constants;

/**
 * Intake subsystem.
 */
public class Intake {
    // Single motor controls intake feeder wheels
    private final Motor _intakeMotor = new Motor();

    /**
     * Creates a new Intake subsystem.
     */
    public Intake() {}

    /** 
     * Runs the intake mechanism to collect a game piece.
     * Spins at full power for Constants.intakeRunTimeSeconds, then stops.
     */
    public Command intake() {
        // TODO
    }
}