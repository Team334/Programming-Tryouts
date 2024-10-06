package src.subsystems;

import lib.Motor;

public class Drive {
    // the left and right wheel motors
    private final Motor _leftMotor = new Motor();
    private final Motor _rightMotor = new Motor();

    /**
     * Drive straight the specified distance in meters at MAX translational speed.
     * 
     * @param distance The distance to drive (5 = 5 meters forwards, -5 = 5 meters backwards).
     */
    public void driveDistance(double distance) {
        System.out.println("DRIVING " + distance + " METERS");
        
        // SUGGESTION:
        // find wheel speeds for max translational speed using Util.toWheelSpeeds()
        // set motors to that speed
        // sleep for a calculated amount of time
        // set motors to 0 speed
        // MAKE SURE TO UTILIZE ANY OF THE CONSTANTS / UTILITY FUNCTIONS
    }

    /** 
     * Turns the robot the specified amount of degrees at MAX rotational speed.
     * 
     * @param degrees The degrees to turn (90 = 90 degrees clockwise, -90 = 90 degrees counter-clockwise).
     */
    public void turnDegrees(double degrees) {
        System.out.println("TURNING " + degrees + " DEGREES");
     
        // SUGGESTION:
        // find wheel speeds for max rotational speed using Util.toWheelSpeeds()
        // set motors to that speed
        // sleep for a calculated amount of time
        // set motors to 0 speed
        // MAKE SURE TO UTILIZE AND OF THE CONSTANTS / UTILITY FUNCTIONS
    }
}
