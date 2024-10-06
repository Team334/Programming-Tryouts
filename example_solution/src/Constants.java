package src;

import lib.Motor;

public class Constants {
    /** The track width between the left and right wheels in meters. */
    public static final double trackWidthMeters = 1;

    /** The circumference of the drive wheels in meters. */
    private static final double wheelCirumferenceMeters = Math.PI * 2 * 0.05;

    /** The maximum wheel/drivetrain speed in meters per second. */
    public static final double maxWheelSpeedMetersPerSecond = Motor.MAX_SPEED * wheelCirumferenceMeters;

    /** The maximum translation speed in meters per second of the drivetrain. */
    public static final double maxTranslationMetersPerSecond = maxWheelSpeedMetersPerSecond;

    /** The maximum rotational speed in degrees per second of the drivetrain. */
    public static final double maxOmegaDegreesPerSecond = 1080;
}
