package lib;

/**
 * A simple LED strip on the robot.
 */
public class LEDs {
    private String _color = "OFF";

    /**
     * Sets the LED color. Any string is fine (e.g. "RED", "GREEN", "BLUE", "OFF") — this
     * doesn't validate against real hardware colors.
     */
    public void setColor(String color) {
        if (!color.equals(_color)) {
            _color = color;
            System.out.println("LEDS: " + color);
        }
    }

    /** Gets the currently-set color. */
    public String getColor() {
        return _color;
    }
}