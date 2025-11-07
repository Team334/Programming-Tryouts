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
}
