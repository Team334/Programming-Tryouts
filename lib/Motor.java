package lib;

public class Motor {
    private double _motorPower;
    private int _id;

    public Motor(int id) {
        _id = id;
    }

    /**
     * Sets the power of the motor [-1, +1].
     */
    public void setPower(double power) {
        _motorPower = Util.clamp(power, -1, 1);
    }

    /**
     * Returns the speed of the motor in rotations per second.
     */
    public double getSpeed() {
        return _motorPower * Constants.motorMaxSpeed;
    }
}