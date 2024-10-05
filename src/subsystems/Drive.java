package src.subsystems;

import lib.Motor;
import lib.Util;
import lib.Util.DriveSpeeds;

public class Drive {
    private final Motor _leftMotor = new Motor();
    private final Motor _rightMotor = new Motor();

    /** The circumference of each drive wheel .*/
    private final double WHEEL_CIRCUMFERENCE = Math.PI * 2 * 0.05;
    private final double MAX_SPEED = (Motor.MAX_SPEED * WHEEL_CIRCUMFERENCE) / 60; // max speed m/s

    public final double TRACK_WIDTH = 100;

    /** Drive straight the specified distance in meters at the specified speed (m/s). */
    public void driveDistance(double distance) {
        System.out.println("DRIVING " + distance + " METERS");

        double speed = 1;

        _leftMotor.setPower(speed / MAX_SPEED);
        _rightMotor.setPower(speed / MAX_SPEED);

        Util.sleep(Math.abs(distance / speed));

        _leftMotor.setPower(0);
        _rightMotor.setPower(0);
    }

    /** Turns the specified degrees. */
    public void turnDegrees(double degrees) {
        System.out.println("TURNING " + degrees + " DEGREES");
        System.out.println(1 / MAX_SPEED);

        double leftSpeed = 1;
        double rightSpeed = -1;

        DriveSpeeds speeds = Util.forwardKinematics(leftSpeed, rightSpeed, TRACK_WIDTH);

        System.out.println(speeds.omega);

        _leftMotor.setPower(leftSpeed / MAX_SPEED);
        _rightMotor.setPower(rightSpeed / MAX_SPEED);

        Util.sleep(Math.abs(degrees / speeds.omega));

        _leftMotor.setPower(0);
        _rightMotor.setPower(0);
    }
}
