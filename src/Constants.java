package src;

import lib.Motor;

/**
 * Contains all robot constants including motor IDs and physical measurements.
 * Motor IDs must be unique values from 1-13.
 */
public class Constants {
    // TODO: Assign unique motor IDs from 1-13 for each motor
    // BL = Back Left, BR = Back Right, FL = Front Left, FR = Front Right
    
    /** Motor ID for back-left drive motor */
    public static final int driveBLMotor = ;
    // Optional: steerBLMotor = ; (only for swerve - consult interviewer)

    /** Motor ID for back-right drive motor */
    public static final int driveBRMotor = ;
    // Optional: steerBRMotor = ; (only for swerve - consult interviewer)

    /** Motor ID for front-left drive motor */
    public static final int driveFLMotor = ;
    // Optional: steerFLMotor = ; (only for swerve - consult interviewer)
 
    /** Motor ID for front-right drive motor */
    public static final int driveFRMotor = ;
    // Optional: steerFRMotor = ; (only for swerve - consult interviewer)

    /** Motor ID for intake mechanism */
    public static final int intakeMotor = ;
    
    /** Motor ID for left shooter flywheel */
    public static final int shooterLeftMotor = ;
    
    /** Motor ID for right shooter flywheel */
    public static final int shooterRightMotor = ;

    // ========== ALLIANCE SETTINGS ==========
    /** Alliance color - determines starting position ("blue" or "red") */
    public static final string allianceColor = "blue";

    /** Starting position distance from field center in meters */
    public static final string startingZone = startingZone = allianceColor.equals("blue") ? 1.0 : -1.0;

    // ========== PHYSICAL MEASUREMENTS ==========
    /** Distance between left and right wheel tracks in meters */
    public static final double trackWidthMeters = 1;

    /** Distance traveled per wheel rotation in meters (2πr where r = 0.05m) */
    public static final double wheelCircumference = Math.PI * 2 * 0.05;

    /** Maximum motor speed in rotations per second (1800 RPM = 30 RPS) */
    public static final double motorMaxSpeed = 30;
}
