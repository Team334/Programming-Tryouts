package src;

import src.subsystems.Drive;
import src.subsystems.Intake;
import src.subsystems.Shooter;

class Robot {
  // instances of each subsystem
  private final Drive _drive = new Drive();
  private final Shooter _shooter = new Shooter();
  private final Intake _intake = new Intake();

  /**
   * Code to run during autonomous.
   */
  private void autonomous() {
    // write your autonomous routine here:

    // SHOOT CALL
    // DRIVE DISTANCE CALL (5 meters backwards) 
    // INTAKE CALL
    // DRIVE DISTANCE CALL (5 meters forwards)
    // SHOOT CALL
    // DRIVE DISTANCE CALL (5 meters backwards)
    // TURN DEGREES CALL (90 degrees clockwise)
    // DRIVE DISTANCE CALL (2 meters backwards)
    // INTAKE CALL
  }


  public static void main(String[] args) {
    Robot robot = new Robot();
    
    robot.autonomous();
  }
}
