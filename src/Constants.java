package src;

import lib.FieldPosition;
import lib.Motor;

/**
 * Contains all robot constants.
 */
public class Constants {
    /** Distance between left and right sides of the robot. */
    private static final double trackWidthMeters = 1;

    /** Distance traveled per wheel rotation. */
    private static final double wheelCircumference = 0.3;

    /** Max robot linear speed in m/s. */
    public static final double maxRobotSpeedMetersPerSecond = Motor.maxSpeedRPS * wheelCircumference;
    
    /** Max robot angular speed in deg/s. */
    public static final double maxRobotAngularSpeedDegreesPerSecond = Math.toDegrees(2 * maxRobotSpeedMetersPerSecond / trackWidthMeters);

    /** The scoring position. */
    public static final FieldPosition scoringPosition = new FieldPosition(10, 10, 10);
}
