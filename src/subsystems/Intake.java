package src.subsystems;

import lib.Motor;

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
     * Operates for 2 seconds at full power.
     */
    public void intake() {
        System.out.println("INTAKING");

        // TODO
    }
}
