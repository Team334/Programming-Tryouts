package src.mechanisms;


import lib.Commands;
import lib.Mechanism;
import lib.Commands.Command;
import lib.Motor;
import src.Constants;


/**
 * Shooter subsystem.
 */
public class Shooter extends Mechanism {
    // Single motor controls shooter feeder wheels
    private final Motor _shooterMotor = new Motor();


    /**
     * Creates a new Shooter subsystem.
     */
    public Shooter() {
        super("Shooter");
    }


    /**
     * Spins feeders and shoots. Spins at full power for Constants.shooterRunTimeSeconds, then stops.
     */
    public Command shoot() {
        return Commands.run(co -> {
            _shooterMotor.setPower(1);


            co.sleep(Constants.shooterRunTimeSeconds);


            _shooterMotor.setPower(0);
        }).requiring(this).named("Shooter: Shoot");
    }
}

