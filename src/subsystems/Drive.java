package src.subsystems;

import lib.Motor;

public class Drive {
    // the left and right wheel motors
    private final Motor _blMotor;
    private final Motor _brMotor;
    private final Motor _frMotor;
    private final Motor _flMotor;

    public Drive(int blMotorId, int brMotorId, int frMotorId, int flMotorId) {
        _blMotor = new Motor(blMotorId);
        _brMotor = new Motor(brMotorId);
        _frMotor = new Motor(frMotorId);
        _flMotor = new Motor(flMotorId);
    }

    public static double drive(
        double flSpeed, double frSpeed, 
        double blSpeed, double brSpeed, 
        double wheelCircumference, double seconds
    ) {
        // Code here 
        return ;
    }

    public static double rotate(
        double flSpeed, double frSpeed,
        double blSpeed, double brSpeed,
        doube trackWidthMeters, double wheelCircumference, 
        double seconds
    ) {
        // Code here
        return ;
    }

    public static double toSecondsForRotation(
        double angleDeg, double flSpeed, double frSpeed, 
        double blSpeed, double brSpeed, double wheelCircumference, 
        double trackWidth
    ) {
      // code here
      return ;
  }

}
