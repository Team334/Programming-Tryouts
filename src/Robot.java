package src;

import src.subsystems.Drive;

class Robot {
  private Drive _drive = new Drive();

  /**
   * Code to run during autonomous.
   */
  private void autonomous() {
    // write your autonomous routine here.

    // SHOOT
    _drive.driveDistance(-1, 5, 1);
    // INTAKE
    _drive.driveDistance(1, 5, 1);
    // SHOOT
    _drive.driveDistance(-1, 5, 1);
    // TURN 90 DEG
    _drive.driveDistance(1, 2, 1);
    // INTAKE
  }


  public static void main(String[] args) {
    Robot robot = new Robot();
    
    robot.autonomous();
  }
}
