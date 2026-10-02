package src.mechanisms;


import lib.Commands;
import lib.Mechanism;
import lib.Commands.Command;
import lib.Motor;
import src.Constants;


/**
 * Hopper subsystem.
 */
public class Hopper extends Mechanism {
    // Two motors control feeder wheels on the left and right sides of the hopper
    private final Motor _leftMotor = new Motor();
    private final Motor _rightMotor = new Motor();


    /**
     * Creates a new Hopper subsystem.
     */
    public Hopper() {
        super("Hopper");
    }


    /**
     * Spins both feeder motors at full power to index game pieces toward the shooter,
     * for Constants.hopperIndexTimeSeconds, then stops.
     */
    public Command index() {
        return Commands.run(co -> {
            _leftMotor.setPower(1);
            _rightMotor.setPower(1);


            co.sleep(Constants.hopperIndexTimeSeconds);


            _leftMotor.setPower(0);
            _rightMotor.setPower(0);
        }).requiring(this).named("Hopper: Index");
    }
}
