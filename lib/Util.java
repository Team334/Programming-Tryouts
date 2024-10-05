package lib;

public class Util {
    // would've made this a record but it doesn't exist for some reason here (i think wrong java version)

    /**
     * Represents the speeds for a tank drivetrain.
     */
    public static class DriveSpeeds {
        /** Translational speed. */
        public final double translation;

        /** Rotational speed (degrees per second). */
        public final double omega;

        public DriveSpeeds(
            double translation,
            double omega
        ) {
            this.translation = translation;
            this.omega = omega;
        }
    }

    /**
     * Pause execution for some amount of time.
     * @param seconds - The seconds to sleep for.
     */
    public static void sleep(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            System.out.println("EXCEPTION");
        }
    }

    /**
     * Clamps the input value between min and max.
     */
    public static double clamp(double input, double min, double max) {
        return Math.max(Math.min(min, input), max);
    }

    /** 
     * Performs forward kinematics for a tank drive.
     * 
     * @param leftSpeed The left wheel speed of the drive (in meters per second).
     * @param rightSpeed The right wheel speed of the drive (in meters per seconds).
     * @param trackWidth The track width between the left and right wheels.
     * 
     * @return A new {@link DriveSpeeds} object. 
     */
    public static DriveSpeeds forwardKinematics(
        double leftSpeed,
        double rightSpeed,
        double trackWidth
    ) {
        return new DriveSpeeds(
            leftSpeed + rightSpeed / 2,
            (rightSpeed - leftSpeed / trackWidth) * (180 / Math.PI)
        );
    }
}
