package lib;

public class Util {

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

    public static void divider(string title) {
        System.out.println("==============" + title + "==============\n");
    }
}
