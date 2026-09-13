package src.mechanisms;

import lib.Commands;
import lib.LEDs;
import lib.Mechanism;

/**
 * [OPTIONAL — if you have time]
 * 
 * Makes the robot's LEDs reflect whatever's currently running: one color while intaking,
 * another while indexing, another while shooting, some default/idle color when nothing's
 * running, and it's up to you what happens when more than one of those is running at once.
 * 
 * Commands.owner(mechanism) tells you which Command currently owns a given mechanism (or
 * null if it's idle right now)
 */
public class LED extends Mechanism{
    private final LEDs _leds = new LEDs();

    private final Drive _drive;
    private final Intake _intake;
    private final Hopper _hopper;
    private final Shooter _shooter;

    public LED(Drive drive, Intake intake, Hopper hopper, Shooter shooter) {
        super("LED");

        _drive = drive;
        _intake = intake;
        _hopper = hopper;
        _shooter = shooter;
    }

    /**
     * Call this regularly (e.g. once per simulation tick, or once per robot loop) to keep
     * the LEDs in sync with whatever's currently running.
     */
    public void update() {
        // TODO: use Commands.owner(_intake) / Commands.owner(_hopper) / Commands.owner(_shooter)
        // / Commands.owner(_drive) to figure out what's running right now, and call
        // _leds.setColor(...) with something that reflects it.
    }

    /** The LEDs' current color, e.g. for display in the simulation. */
    public String getColor() {
        return _leds.getColor();
    }
}