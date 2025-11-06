package src;

import lib.FieldPosition;

/**
 * Contains all robot constants.
 */
public class Constants {
    /** Distance between left and right sides of the robot. */
    public static final double trackWidthMeters = 1;

    /** Distance traveled per wheel rotation. */
    public static final double wheelCircumference = 0.3;

    /** The scoring position. */
    public static final FieldPosition scoringPosition = new FieldPosition(10, 10, 10);
}
