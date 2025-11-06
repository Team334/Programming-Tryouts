package src.subsystems;

import lib.Motor;

/**
 * Shooter subsystem.
 */
public class Shooter {
    // Single motor controls shooter feeder wheels
    private final Motor _shooterMotor = new Motor();

    /**
     * Creates a new Shooter subsystem.
     */
    public Shooter() {}

    /** 
     * Spins feeders and shoots for 4 seconds.
     */
    public void shoot() {
        System.out.println("SHOOTING");

        // TODO

        // Motor should spin at full power
        // Use Util.sleep() to maintain spin for required duration
        // Stop motor when done
    }
}
