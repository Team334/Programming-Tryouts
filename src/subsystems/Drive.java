package src.subsystems;

import lib.Motor;
import lib.Util;
import lib.Util.DriveSpeeds;
import lib.Util.WheelSpeeds;
import src.Constants;

public class Drive {
    private final Motor _leftMotor = new Motor();
    private final Motor _rightMotor = new Motor();

    public void test() {
        System.out.println(Constants.maxTranslationMetersPerSecond);

        System.out.println(
            Util.toDriveSpeeds(new WheelSpeeds(Constants.maxTranslationMetersPerSecond, Constants.maxTranslationMetersPerSecond), Constants.trackWidthMeters).translationMetersPerSecond
        );
    }

    /** Drive straight the specified distance in meters at the specified speed (m/s). */
    public void driveDistance(double distance) {
        System.out.println("DRIVING " + distance + " METERS");

        WheelSpeeds wheelSpeeds = Util.toWheelSpeeds(
            new DriveSpeeds(Constants.maxTranslationMetersPerSecond * Util.sign(distance), 0),
            Constants.trackWidthMeters
        );

        _leftMotor.setPower(wheelSpeeds.leftMetersPerSecond / Constants.maxWheelSpeedMetersPerSecond);
        _rightMotor.setPower(wheelSpeeds.rightMetersPerSecond / Constants.maxWheelSpeedMetersPerSecond);

        Util.sleep(Math.abs(distance / Constants.maxTranslationMetersPerSecond));

        _leftMotor.setPower(0);
        _rightMotor.setPower(0);
    }

    /** Turns the specified degrees. */
    public void turnDegrees(double degrees) {
        System.out.println("TURNING " + degrees + " DEGREES");
     
        WheelSpeeds wheelSpeeds = Util.toWheelSpeeds(
            new DriveSpeeds(0, Constants.maxOmegaDegreesPerSecond), 
            Constants.trackWidthMeters
        );

        _leftMotor.setPower(wheelSpeeds.leftMetersPerSecond / Constants.maxWheelSpeedMetersPerSecond);
        _rightMotor.setPower(wheelSpeeds.rightMetersPerSecond / Constants.maxWheelSpeedMetersPerSecond);

        Util.sleep(Math.abs(degrees / Constants.maxOmegaDegreesPerSecond));

        _leftMotor.setPower(0);
        _rightMotor.setPower(0);
    }
}
