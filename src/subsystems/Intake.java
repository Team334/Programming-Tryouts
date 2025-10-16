package src.subsystems;

import lib.Motor;

public class Intake {
    // the intake motor
    private final Motor _intakeMotor;

    public Intake(int intakeMotorId) {
        _intakeMotor = new Motor(intakeMotorId);
    }

    /** 
     * Intakes the game piece for 2 seconds.
     */
    public void intake() {
        System.out.println("INTAKING");

        // SUGGESTION:
        // power intake motor
        // sleep for the necessary amount of time
        // turn off intake motor
    }
}
