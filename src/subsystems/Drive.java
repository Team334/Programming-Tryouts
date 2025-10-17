package src.subsystems;

import lib.Motor;

/**
 * Drive subsystem controlling the four-wheel drive system.
 * Handles both straight-line movement and rotation.
 */
public class Drive {
    // Four drive motors for independent wheel control
    private final Motor _blMotor;
    private final Motor _brMotor;
    private final Motor _frMotor;
    private final Motor _flMotor;

    /**
     * Creates a new Drive subsystem with four motors.
     * 
     * @param blMotorId Motor ID for back-left wheel
     * @param brMotorId Motor ID for back-right wheel
     * @param frMotorId Motor ID for front-right wheel
     * @param flMotorId Motor ID for front-left wheel
     */
    public Drive(int blMotorId, int brMotorId, int frMotorId, int flMotorId) {
        _blMotor = new Motor(blMotorId);
        _brMotor = new Motor(brMotorId);
        _frMotor = new Motor(frMotorId);
        _flMotor = new Motor(flMotorId);
    }

    /**
     * Calculates the time required to travel a specific distance.
     * 
     * @param distance Target distance in meters
     * @param flSpeed Front-left wheel speed in RPS
     * @param frSpeed Front-right wheel speed in RPS
     * @param blSpeed Back-left wheel speed in RPS
     * @param brSpeed Back-right wheel speed in RPS
     * @param wheelCircumference Distance per wheel rotation in meters
     * @return Time in seconds needed to travel the distance
     */
    public static double toSecondsForDistance(double distance, 
        double flSpeed, double frSpeed, 
        double blSpeed, double brSpeed, 
        double wheelCircumference
    ) {    
        // TODO: Calculate average speed and convert distance to time
        // Hint: Average left side, average right side, then average both
        // Hint: Convert RPS to meters/second using wheelCircumference
        return ;
    }

    /**
     * Calculates the distance traveled given wheel speeds and elapsed time.
     * 
     * @param flSpeed Front-left wheel speed in RPS
     * @param frSpeed Front-right wheel speed in RPS
     * @param blSpeed Back-left wheel speed in RPS
     * @param brSpeed Back-right wheel speed in RPS
     * @param wheelCircumference Distance per wheel rotation in meters
     * @param seconds Time elapsed in seconds
     * @return Distance traveled in meters
     */
    public static double drive(
        double flSpeed, double frSpeed, 
        double blSpeed, double brSpeed, 
        double wheelCircumference, double seconds
    ) {
        // TODO: Calculate average speed and multiply by time
        // Hint: Similar to toSecondsForDistance but solve for distance
        return ;
    }

    /**
     * Calculates the rotation angle achieved given wheel speeds and elapsed time.
     * 
     * @param flSpeed Front-left wheel speed in RPS
     * @param frSpeed Front-right wheel speed in RPS
     * @param blSpeed Back-left wheel speed in RPS
     * @param brSpeed Back-right wheel speed in RPS
     * @param trackWidthMeters Distance between left and right wheels
     * @param wheelCircumference Distance per wheel rotation in meters
     * @param seconds Time elapsed in seconds
     * @return Degrees rotated (positive = clockwise)
     */
    public static double rotate(
        double flSpeed, double frSpeed,
        double blSpeed, double brSpeed,
        doube trackWidthMeters, double wheelCircumference, 
        double seconds
    ) {
        // TODO: Calculate arc length difference between sides, then convert to angle
        // Hint: Left and right sides move in opposite directions during rotation
        // Hint: Use Util.radToDeg() to convert result
        return ;
    }

    /**
     * Calculates the time required to rotate a specific angle.
     * 
     * @param angleDeg Target rotation angle in degrees (positive = clockwise)
     * @param flSpeed Front-left wheel speed in RPS
     * @param frSpeed Front-right wheel speed in RPS
     * @param blSpeed Back-left wheel speed in RPS
     * @param brSpeed Back-right wheel speed in RPS
     * @param wheelCircumference Distance per wheel rotation in meters
     * @param trackWidth Distance between left and right wheels
     * @return Time in seconds needed to complete the rotation
     */
    public static double toSecondsForRotation(
        double angleDeg, double flSpeed, double frSpeed, 
        double blSpeed, double brSpeed, double wheelCircumference, 
        double trackWidth
    ) {
      // TODO: Convert angle to arc length, then calculate time
      // Hint: Use Util.degToRad() to convert degrees
      // Hint: Arc length = angle (radians) × radius
      return ;
  }

}
