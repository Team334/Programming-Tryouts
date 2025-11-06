package src.subsystems;

import lib.FieldPosition;
import lib.Motor;
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

        _latestPosition = position;
    }

    /**
     * Rotates the chassis by the given degrees.
     */
    private void rotateDegrees(double degreesToRotate) {
        // TODO
    }

    /**
     * Drives forwards at max speed by the given meters.
     */
    private void driveMeters(double metersToDrive) {
        // TODO 
    }
}
