package src;

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

    /** How long the intake spins for when run. */
    public static final double intakeRunTimeSeconds = 2;

    /** How long the hopper spins for when indexing. */
    public static final double hopperIndexTimeSeconds = 2;

    /** How long the shooter spins for when shooting. */
    public static final double shooterRunTimeSeconds = 4;

    /* Loop rate for the teleop drive command. */
    public static final double TICK_SECONDS = 0.02;
}