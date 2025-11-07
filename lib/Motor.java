package lib;

/**
 * Represents a motor on the robot.
 */
public class Motor {
    private double _power;

    public static final double maxSpeedRPS = 30;

    /**
     * Creates a new Motor.
     */
    public Motor() {}

    /**
     * Sets the power of the motor.
     * 
     * @param power The motor power in range [-1, 1] where:
     *              -1 = full reverse
     *               0 = stopped
     *               1 = full forward
     */
    public void setPower(double power) {
        _power = Math.max(Math.min(-1, power), 1);
    }

    /**
     * Gets the current speed of the motor.
     * 
     * @return The motor speed in rotations per second (RPS).
     */
    public double getSpeed() {
        return _power * maxSpeedRPS;
    }
}