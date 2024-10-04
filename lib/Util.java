package lib;

public class Util {
    /**
     * Pause execution for some amount of time.
     * @param millis - The milliseconds to sleep for.
     */
    public static void sleep(double millis) {
        try {
            Thread.sleep((long) millis);
        } catch (InterruptedException e) {}
    }

    /**
     * Clamps the input value between min and max.
     */
    public static double clamp(double input, double min, double max) {
        return Math.max(Math.min(min, input), max);
    }
}
