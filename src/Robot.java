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
   * Code to run during autonomous. Starting on red alliance
   * The robot always starts facing right or left depending on the alliance color (right is blue, left is red)
   * Note: Robot should never go past 8m in horizontal distance from the center of the field or 4.5m vertical distance from the center of the field.
   */
  private void autonomous() {
    // write your autonomous routine here, 
    // it doesn't have to be the same steps below:

    // SHOOT CALL

    // DRIVE DISTANCE CALL (2 meters foward) 

    // INTAKE CALL

    // DRIVE DISTANCE CALL (3 meters forward)

    // SHOOT CALL

    // DRIVE DISTANCE CALL (4 meters backwards)

    // TURN DEGREES CALL (90 degrees clockwise)

    // DRIVE DISTANCE CALL (2 meters backwards)

    // INTAKE CALL

    // Print current position or displacement from the origin (center of the field)

  }


  public static void main(String[] args) {
    Robot robot = new Robot();
    
    robot.autonomous();
  }
}
