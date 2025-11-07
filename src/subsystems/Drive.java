package src.subsystems;

import lib.FieldPosition;
import lib.Motor;
import lib.Util;
import lib.VisionSystem;
import src.Constants;

/**
 * Drive subsystem.
 */
public class Drive {
    // Four drive motors for independent wheel control
    private final Motor _flMotor = new Motor();
    private final Motor _frMotor = new Motor();
    private final Motor _brMotor = new Motor();
    private final Motor _blMotor = new Motor();

    private final VisionSystem _visionSystem = new VisionSystem();

    private FieldPosition _latestPosition = new FieldPosition();

    /**
     * Creates a new Drive subsystem.
     */
    public Drive() {}

    /**
     * Drives to the position (as returned by the vision system) of a game piece.
     */
    public void driveToGamePiecePosition() {
        System.out.println("DRIVING TO GAME PIECE POSITION");

        FieldPosition gamePiecePosition = _visionSystem.getGamePiecePosition();

        driveToPosition(gamePiecePosition);
    }

    /**
     * Drives to the position of the scoring location.
     */
    public void driveToScoringPosition() {
        System.out.println("DRIVING TO SCORING POSITION");

        driveToPosition(Constants.scoringPosition);
    }

    /**
     * Drives to the given position.
     * 
     * First, the chassis rotates to face the position, then it drive to it, and then it rotates again to the position's heading.
     */
    private void driveToPosition(FieldPosition position) {
        // TODO

        // Rotate to face the position
        // Drive to the position
        rotateDegrees(_latestPosition.angleTo(position));
        driveMeters(_latestPosition.distanceTo(position));
        
        _latestPosition = new FieldPosition(
            position.getX(),
            position.getY(),
            _latestPosition.getHeading() + _latestPosition.angleTo(position)
        ); // DO NOT DELETE

        // Rotate to the position's heading
        rotateDegrees(_latestPosition.angleTo(position));

        _latestPosition = position; // DO NOT DELETE

        System.out.println("ARRIVED AT POSITION: (" + _latestPosition.getX() + ", " + _latestPosition.getY() + ", " + _latestPosition.getHeading() + ")"); // DO NOT DELETE
    }


    /**
     * Rotates the chassis at max speed by the given amount of degrees.
     */
    private void rotateDegrees(double degreesToRotate) {
        // TODO

        // Set all the motors to max power, making sure to include the correct signs for rotation
        // Util.sleep() for the right amount of time
        // Stop all the motors

        _flMotor.setPower(-Math.signum(degreesToRotate));
        _blMotor.setPower(-Math.signum(degreesToRotate));
        _frMotor.setPower(Math.signum(degreesToRotate));
        _brMotor.setPower(Math.signum(degreesToRotate));

        Util.sleep(Math.abs(degreesToRotate / Constants.maxRobotAngularSpeedDegreesPerSecond));

        _flMotor.setPower(0.0);
        _blMotor.setPower(0.0);
        _frMotor.setPower(0.0);
        _brMotor.setPower(0.0);
    }

    /**
     * Drives forwards at max speed by the given amount of meters.
     */
    private void driveMeters(double metersToDrive) {
        // TODO 

        // Set all the motors to max power, making sure to include the correct sign for forwards/backwards
        // Util.sleep() for the right amount of time
        // Stop all the motors

        _flMotor.setPower(Math.signum(metersToDrive));
        _blMotor.setPower(Math.signum(metersToDrive));
        _frMotor.setPower(Math.signum(metersToDrive));
        _brMotor.setPower(Math.signum(metersToDrive));

        Util.sleep(Math.abs(metersToDrive / Constants.maxRobotSpeedMetersPerSecond));

        _flMotor.setPower(0.0);
        _blMotor.setPower(0.0);
        _frMotor.setPower(0.0);
        _brMotor.setPower(0.0); 
    }
}
