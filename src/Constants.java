package src;

import lib.Motor;

public class Constants {
    /** The track width between the left and right wheels in meters. */
    // Steer is for rotating, drive is driving straight. IF YOU CONSIDERING STEER MOTORS, Please consult an interviewer before you do!!!
    // BL, BR, FL, FR is back left, back right, front left and front right respectively. 
    // You can decide which motor ids to use from 1-13.
    public static final int driveBLMotor = ;
    // steerBLMotor = ;

    public static final int driveBRMotor = ;
    // steerBRMotor = ;

    public static final int driveFLMotor = ;
    // steerFLMotor = ;
 
    public static final int driveFRMotor = ;
    // steerFRMotor = ;

    public static final int intakeMotor = ;
    public static final int shooterLeftMotor = ;
    public static final int shooterRightMotor = ;

    // You decide which alliance color. Default is blue
    public static final string allianceColor = "blue";

    // Starting place is the distance (1m) from the center of the field to the black solid line 
    public static final string startingZone = startingZone = allianceColor.equals("blue") ? 1.0 : -1.0;

    // The tracks are like the treads of the tires
    public static final double trackWidthMeters = 1;

    // Total distance traveled in 1 rotation of the wheel
    public static final double wheelCircumference = Math.PI * 2 * 0.05;

    // Max rotation per second the motor can go (1800 rpm)
    public static final double motorMaxSpeed = 30;
}
