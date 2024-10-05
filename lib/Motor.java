package lib;

public class Motor {
    private double _motorPower;

    /** The max speed of this motor in RPM. */
    public final static double MAX_SPEED = 100; 

    /**
     * Sets the power of the motor [-1, +1].
     */
    public void setPower(double power) {
        _motorPower = Util.clamp(power, -1, 1);
        System.out.println(_motorPower);
    }

    /**
     * Returns the speed of the motor in rotations per minute.
     */
    public double getSpeed() {
        return _motorPower * MAX_SPEED;
    }
}