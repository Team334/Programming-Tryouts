package src;

import src.subsystems.Drive;
import src.subsystems.Intake;
import src.subsystems.Shooter;

class Robot {
  private final Drive _drive = new Drive();
  private final Shooter _shooter = new Shooter();
  private final Intake _intake = new Intake();

  /**
   * Code to run during autonomous.
   */
  private void autonomous() {
    // write your autonomous routine here:

    // _drive.test();

    _shooter.shoot();
    _drive.driveDistance(-5);
    _intake.intake();
    _drive.driveDistance(5);
    _shooter.shoot();
    _drive.driveDistance(-5);
    _drive.turnDegrees(90);
    _drive.driveDistance(2);
    _intake.intake();
  }


  public static void main(String[] args) {
    Robot robot = new Robot();
    
    robot.autonomous();
  }
}
