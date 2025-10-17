package src.subsystems;

import lib.Motor;
import lib.Util;

/**
 * Shooter subsystem for launching game pieces into the Reef.
 * Uses flywheels for consistent shooting. Maximum range: 1 meter.
 */
public class Shooter {
    // Motors for left and right flywheels
    private final Motor _leftShooterMotor;
    private final Motor _rightShooterMotor;

    /**
     * Creates a new Shooter subsystem.
     * 
     * @param leftShooterMotorId Motor ID for left flywheel
     * @param rightShooterMotorId Motor ID for right flywheel
     */
    public Shooter(int leftShooterMotorId, int rightShooterMotorId) {
        _leftShooterMotor = new Motor(leftShooterMotorId);
        _rightShooterMotor = new Motor(rightShooterMotorId);
    }

    /** 
     * Launches a game piece into the Reef.
     * Spins up flywheels and shoots for 3 seconds.
     */
    public void shoot() {
        System.out.println("SHOOTING");

        // TODO: Implement shooting sequence
        // Hint: Both motors should spin at full power in the same direction
        // Hint: Use Util.sleep() to maintain spin for required duration
        // Hint: Stop both motors when done
    }
}
