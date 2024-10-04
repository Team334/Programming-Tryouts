package src.subsystems;

import lib.Motor;
import lib.Util;

public class Drive {
    private final Motor _leftMotor = new Motor();
    private final Motor _rightMotor = new Motor();

    private final double WHEEL_CIRCUMFERENCE = Math.PI * 2 * 0.05;
    private final double MAX_SPEED = (Motor.MAX_SPEED * WHEEL_CIRCUMFERENCE) / 60; // max speed m/s

    /** Drive straight the specified distance in meters at the specified speed (m/s). */
    public void driveDistance(int direction, double distance, double speed) {
        _leftMotor.setPower(direction * speed / MAX_SPEED);
        _rightMotor.setPower(direction * speed / MAX_SPEED);

        Util.sleep(distance / speed);

        _leftMotor.setPower(0);
        _rightMotor.setPower(0);
    }
}
