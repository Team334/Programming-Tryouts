package src.subsystems;

import lib.Motor;
import lib.Util;

public class Shooter {
    // the shooter motor
    private final Motor _leftShooterMotor;
    private final Motor _rightShooterMotor;

    public Shooter(int leftShooterMotorId, int rightShooterMotorId) {
        _leftShooterMotor = new Motor(leftShooterMotorId);
        _rightShooterMotor = new Motor(rightShooterMotorId);
    }

    /** Shoot the game piece for 3 seconds. */
    public void shoot() {
        System.out.println("SHOOTING");

        // SUGGESTION:
        // power left and right shooter motors 
        // sleep for the necessary amount of time
        // turn off shooter motor
    }
}
