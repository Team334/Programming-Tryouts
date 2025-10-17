package lib;

/**
 * Represents a motor on the robot.
 * Each motor has a unique ID and can be controlled by setting its power.
 */
public class Motor {
    private double _motorPower;
    private int _id;

    /**
     * Creates a new Motor with the specified ID.
     * 
     * @param id The unique motor ID (1-13)
     */
    public Motor(int id) {
        _id = id;
    }

    /**
     * Sets the power of the motor.
     * 
     * @param power The motor power in range [-1, 1] where:
     *              -1 = full reverse
     *               0 = stopped
     *               1 = full forward
     */
    public void setPower(double power) {
        _motorPower = Util.clamp(power, -1, 1);
    }

    /**
     * Gets the current speed of the motor.
     * 
     * @return The motor speed in rotations per second (RPS).
     *         Calculated as motorPower × motorMaxSpeed
     */
    public double getSpeed() {
        return _motorPower * Constants.motorMaxSpeed;
    }
}