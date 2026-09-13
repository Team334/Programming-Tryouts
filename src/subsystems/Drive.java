package src.subsystems;

import lib.Commands;
import lib.Commands.Command;
import lib.FieldPosition;
import lib.Motor;
import src.Constants;

import java.util.function.DoubleSupplier;

/**
 * Drive subsystem.
 */
public class Drive {

    // Four drive motors for independent wheel control
    private final Motor _flMotor = new Motor();
    private final Motor _frMotor = new Motor();
    private final Motor _brMotor = new Motor();
    private final Motor _blMotor = new Motor();

    // Read from the UI/telemetry thread (e.g. Simulation's paintComponent) while the
    // teleop command's own virtual thread writes it every loop iteration — volatile so
    // those reads always see the latest position instead of a stale cached one.
    private volatile FieldPosition _latestPosition = new FieldPosition();

    /**
     * Creates a new Drive subsystem.
     */
    public Drive() {}

    /**
     * Builds the teleop driving Command: an arcade-style drive loop that reads live
     * forward/rotate power from the given suppliers once per iteration, at roughly
     * TICK_SECONDS intervals, until it's interrupted — e.g. by scheduling some other
     * command that also requires this subsystem.
     *
     * This is meant to be scheduled once as Drive's default command (see Simulation),
     * rather than called directly every tick like the old driveTeleop(...) method was.
     *
     * @param forwardPower Supplies forward power in [-1, 1].
     * @param rotatePower Supplies rotational power in [-1, 1] (positive = counterclockwise).
     */
    public Command driveTeleop(DoubleSupplier forwardPower, DoubleSupplier rotatePower) {
        return Commands.run(co -> {
            while (true) {
                drive(forwardPower.getAsDouble(), rotatePower.getAsDouble(), Constants.TICK_SECONDS);
                co.sleep(Constants.TICK_SECONDS);
            }
        }).requiring(this).named("Drive Teleop");
    }

    /** The robot's current estimated position — used for display/telemetry (e.g. Simulation). */
    public FieldPosition getPosition() {
        return _latestPosition;
    }

    /** 
     * 
     * @param forwardPower Supplies forward power in [-1, 1].
     * @param rotatePower Supplies rotational power in [-1, 1] (positive = counterclockwise).
     * @param dtSeconds Supplies the time elapsed
     */
    private void drive(double forwardPower, double rotatePower, double dtSeconds) {
        // 1. Calculate the power going to each side
        double leftPower = clamp(...);
        double rightPower = clamp(...);

        // 2. Set those powers to the motors to respective side

        // 3. Estimate forward and rotational speed. Check constants.
        double forwardSpeed = ;
        double angularSpeed = ;

        // 4. Update the robot's heading using the angular speed and time elapsed
        double newHeading = ;

        // 5. Convert the new heading to radians
        double headingRad = ;

        // 6. Update position
        _latestPosition = new FieldPosition(
            _latestPosition.getX() + forwardSpeed * Math.cos(headingRad) * dtSeconds,
            _latestPosition.getY() + forwardSpeed * Math.sin(headingRad) * dtSeconds,
            newHeading
        );

        // 7. Set Motor Speeds to 0
    }

    private static double clamp(double power) {
        return Math.max(-1, Math.min(1, power));
    }
}