package src.subsystems;

import lib.Motor;
import lib.Util;

public class Intake {
    private final Motor _intakeMotor = new Motor();

    /** Intakes the note for 2 seconds. */
    public void intake() {
        System.out.println("INTAKING");

        _intakeMotor.setPower(0.8);
        Util.sleep(2);
        _intakeMotor.setPower(0);
    }
}
