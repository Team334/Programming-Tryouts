package src.subsystems;

import lib.Motor;
import lib.Util;

/**
 * Intake subsystem for collecting game pieces from the field.
 * Can intake objects up to 0.8 meters away.
 */
public class Intake {
    // Single motor controls intake wheels
    private final Motor _intakeMotor;

    /**
     * Creates a new Intake subsystem.
     * 
     * @param intakeMotorId Motor ID for the intake mechanism
     */
    public Intake(int intakeMotorId) {
        _intakeMotor = new Motor(intakeMotorId);
    }

    /** 
     * Runs the intake mechanism to collect a game piece.
     * Operates for 2 seconds at full power.
     */
    public void intake() {
        System.out.println("INTAKING");

        // TODO: Implement intake sequence
        // Hint: Set motor power to 1.0 or -1.0 depending on intake direction
        // Hint: Use Util.sleep() to run for the required duration
        // Hint: Stop the motor when done (power = 0)
    }
}
