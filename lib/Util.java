package lib;

public class Util {
    /**
     * Represents the speeds for a tank drivetrain.
     */
    public static class DriveSpeeds {
        /** Translational speed in meters per second. */
        public final double translationMetersPerSecond;

        /** Rotational speed in degrees per second. */
        public final double omegaDegreesPerSecond;

        public DriveSpeeds(
            double translationMetersPerSecond,
            double omegaDegreesPerSecond
        ) {
            this.translationMetersPerSecond = translationMetersPerSecond;
            this.omegaDegreesPerSecond = omegaDegreesPerSecond;
        }
    }

    /**
     * Represents the individual wheel speeds for a tank drivetrain.
     */
    public static class WheelSpeeds {
        /** The speed of the left wheel in meters per second. */
        public final double leftMetersPerSecond;

        /** The speed of the right wheel in meters per second. */
        public final double rightMetersPerSecond;

        public WheelSpeeds(
            double leftMetersPerSecond,
            double rightMetersPerSecond
        ) {
            this.leftMetersPerSecond = leftMetersPerSecond;
            this.rightMetersPerSecond = rightMetersPerSecond;
        }
    }

    public static double degToRad(double deg) {
        return deg * (Math.PI / 180);
    }

    public static double radToDeg(double rad) {
        return rad * (180 / Math.PI);
    }

    /**
     * Pause execution for some amount of time.
     * 
     * @param seconds The seconds to sleep for.
     */
    public static void sleep(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } 
        
        catch (InterruptedException e) {
            System.out.println("UNEXPECTED EXCEPTION");
        }
    }

    /**
     * Clamps the input value between min and max.
     */
    public static double clamp(double input, double min, double max) {
        return Math.max(Math.min(min, input), max);
    }

    /**
     * Returns the sign of the input value. 
     * 
     * @return -1 for negative, 1 for positive, 0 for 0.
     */
    public static double sign(double num) {
        return Math.signum(num);
    }

    /**
     * Performs inverse kinematics for a tank drive, converting drive speeds to wheel speeds.
     * 
     * @param driveSpeeds The desired drive speeds.
     * @param trackWidth The track width between the left and right wheels.
     * 
     * @return A new {@link WheelSpeeds} object.
     */
    public static WheelSpeeds toWheelSpeeds(
        DriveSpeeds driveSpeeds,
        double trackWidth
    ) {
        double omegaRadiansPerSecond = degToRad(driveSpeeds.omegaDegreesPerSecond);

        return new WheelSpeeds(
            driveSpeeds.translationMetersPerSecond - trackWidth / 2 * omegaRadiansPerSecond,
            driveSpeeds.translationMetersPerSecond + trackWidth / 2 * omegaRadiansPerSecond
        );
    }

    /** 
     * Performs forward kinematics for a tank drive, converting wheel speeds to drive speeds.
     * 
     * @param wheelSpeeds The current wheel speeds on the drivetrain.
     * @param trackWidth The track width between the left and right wheels.
     * 
     * @return A new {@link DriveSpeeds} object. 
     */
    public static DriveSpeeds toDriveSpeeds(
        WheelSpeeds wheelSpeeds,
        double trackWidth
    ) {
        return new DriveSpeeds(
            (wheelSpeeds.leftMetersPerSecond + wheelSpeeds.rightMetersPerSecond) / 2,
            radToDeg((wheelSpeeds.rightMetersPerSecond - wheelSpeeds.leftMetersPerSecond) / trackWidth)
        );
    }
}
