package src.subsystems;

import lib.Motor;
import lib.Util;

public class Shooter {
    private final Motor _flywheelMotor = new Motor();

    /** Shoot the game piece for 3 seconds. */
    public void shoot() {
        System.out.println("SHOOTING");

        _flywheelMotor.setPower(0.8);
        Util.sleep(3);
        _flywheelMotor.setPower(0);
    }
}
