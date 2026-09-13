package lib;

/**
 * Utility class containing helper functions.
 */
public class Util {
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
     * Clamps a value between a specified minimum and maximum range: [-1, 1].
     * <p>
     * If the value is less than the minimum, the minimum is returned.
     * If the value is greater than the maximum, the maximum is returned.
     * Otherwise, the value itself is returned.
     * 
     * @param seconds The duration to sleep in seconds (will be clamped 1-135s)
     */
    public static double clamp(double value) {
        return Math.max(-1, Math.min(1, value));
    }
}
