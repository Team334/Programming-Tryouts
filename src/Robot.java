package src;

import src.subsystems.Drive;
import src.subsystems.Intake;
import src.subsystems.Shooter;

/**
 * Main robot class that coordinates all subsystems and executes the autonomous routine.
 */
class Robot {
  // TODO: Initialize subsystems with motor IDs from Constants
  private final Drive _drive = new Drive(...); // Pass 4 motor IDs (BL, BR, FR, FL)
  private final Shooter _shooter = new Shooter(...); // Pass 2 motor IDs (left, right)
  private final Intake _intake = new Intake(...); // Pass 1 motor ID

  /**
   * Autonomous routine executed during the first 15 seconds of the match.
   * Robot starts at alliance starting zone facing toward/away from center.
   * 
   * Field Constraints:
   * - Horizontal: -8m to +8m from center
   * - Vertical: -4.5m to +4.5m from center
   * - Starting position: ±1.0m based on alliance color
   */
  private void autonomous() {
    // TODO: Implement your autonomous routine
    // Hint: Track your position as you move to stay within bounds
    // Hint: Use Util.divider() to make output readable

    // TODO: Drive forward 1 meter
 
    // TODO: Run intake to collect game piece

    // TODO: Drive backwards 3 meters

    // TODO: Shoot the game piece

    // TODO: Drive forward 2 meters

    // TODO: Turn 90 degrees clockwise

    // TODO: Drive forward 2 meters

    // TODO: Print current position from origin as a point (x, y)

  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    Robot robot = new Robot();
    
    robot.autonomous();
  }
}
