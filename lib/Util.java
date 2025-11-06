package lib;

/**
 * Utility class containing helper functions.
 */
public class Util {
    /**
     * Converts degrees to radians.
     * 
     * @param deg Angle in degrees
     * @return Angle in radians
     */
    public static double degToRad(double deg) {
        return deg * (Math.PI / 180);
    }

    /**
     * Converts radians to degrees.
     * 
     * @param rad Angle in radians
     * @return Angle in degrees
     */
    public static double radToDeg(double rad) {
        return rad * (180 / Math.PI);
    }

    /**
     * Pauses execution for a specified duration.
     * 
     * @param seconds The duration to sleep in seconds (will be clamped 1-135s)
     */
    public static void sleep(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } 
        
        catch (InterruptedException e) {
            System.out.println("sleep did not work");
        }
    }

    /**
     * Restricts a value to be within a specified range.
     * 
     * @param input The value to clamp
     * @param min The minimum allowed value
     * @param max The maximum allowed value
     * 
     * @return The clamped value
     */
    public static double clamp(double input, double min, double max) {
        return Math.max(Math.min(min, input), max);
    }
}
