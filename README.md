# Programming Tryouts - Reefscape Robot Challenge

## Overview
The programming tryouts involve coding an autonomous period for a hypothetical Reefscape robot. You will be programming the subsystems and autonomous routine for a robot that must navigate the field, intake game pieces, and shoot them into the Reef.

![Diagram](./Diagram.png)

## Time Limit
**40 minutes** to code and debug your solution.

## The Challenge
The mechanical team has finished building the robot, and you need to program it. The robot has motors with IDs from 1-13. Your robot must:
- Move a set distance using the motors and constants provided
- Intake game pieces from the field
- Shoot game pieces into the Reef

### Match Format
- **Total Time:** 2 minutes and 15 seconds
- **Autonomous Period:** First 15 seconds (no manual control, pre-programmed movements only)
- **Teleoperated Period:** Remaining 2 minutes (manual control to score points)

## Field Layout
The field has a center origin (0, 0) with:
- **Horizontal boundaries:** ±8 meters from center
- **Vertical boundaries:** ±4.5 meters from center
- **Starting zones:** Blue alliance starts at +1.0m, Red alliance starts at -1.0m from center
- **Reef:** The scoring zone where you shoot game pieces

## Robot Specifications

### Subsystems
1. **Drive** - Four-wheel drive system (front-left, front-right, back-left, back-right)
2. **Intake** - Mechanism to pick up game pieces (range: 0.8m)
3. **Shooter** - Dual-motor shooter (range: 1m)

### Constraints
- Maximum of 13 motors
- Maximum 3 motors per drive module
- Robot must stay within field boundaries
- Cannot enter the Reef structure
- Must start in designated alliance zone (blue or red)

## Autonomous Routine Example
Your autonomous routine should accomplish tasks like:
1. Drive forward 1 meter
2. Intake a game piece
3. Drive backwards 3 meters
4. Shoot the game piece
5. Drive forward 2 meters
6. Turn 90 degrees clockwise
7. Drive forward 2 meters
8. Report current position from origin

## Files You'll Work With

### Library Files (lib/)
1. **Motor.java** - Provides motor control functionality (mostly complete)
2. **Util.java** - Utility functions including sleep, clamp, angle conversion, and the divider function

### Source Files (src/)
1. **Constants.java** - Motor IDs and robot physical constants (you need to assign motor IDs)
2. **Robot.java** - Main robot class (implement autonomous routine)
3. **subsystems/Drive.java** - Drive subsystem (implement movement methods)
4. **subsystems/Intake.java** - Intake subsystem (implement intake logic)
5. **subsystems/Shooter.java** - Shooter subsystem (implement shooting logic)

### What You Need to Do
- Assign motor IDs in `Constants.java` (1-13)
- Implement methods in `Util.java` for distance and rotation calculations
- Complete the `Drive`, `Intake`, and `Shooter` subsystem classes
- Write the autonomous routine in `Robot.java`

## API Documentation

### Motor Class (`lib/Motor.java`)

#### `public void setPower(double power)`
Sets the power of the motor.
- **Parameters:** `power` - Value between [-1, 1] where -1 is full reverse, 0 is stopped, 1 is full forward
- **Returns:** None

#### `public double getSpeed()`
Returns the current speed of the motor.
- **Returns:** Speed in rotations per second (e.g., 30 rps × 0.8 power = 24 rps)

### Util Class (`lib/Util.java`)

#### `public static void sleep(double seconds)`
Pause execution for a specified amount of time.
- **Parameters:** `seconds` - The duration to sleep (clamped between 1-135 seconds)
- **Returns:** None

#### `public static double clamp(double value, double min, double max)`
Clamps a value between minimum and maximum bounds.
- **Parameters:** 
  - `value` - The input value
  - `min` - Minimum allowed value
  - `max` - Maximum allowed value
- **Returns:** Clamped value

#### `public static double degToRad(double deg)`
Converts degrees to radians.
- **Parameters:** `deg` - Angle in degrees
- **Returns:** Angle in radians

#### `public static double radToDeg(double rad)`
Converts radians to degrees.
- **Parameters:** `rad` - Angle in radians
- **Returns:** Angle in degrees

#### `public static void divider(String title)`
Prints a formatted divider line with a title.
- **Parameters:** `title` - Text to display in the divider
- **Returns:** None

#### `public static double toSecondsForDistance(...)` *(YOU IMPLEMENT)*
Calculates time needed to travel a distance.
- **Returns:** Seconds required for the desired distance

#### `public static double drive(...)` *(YOU IMPLEMENT)*
Calculates distance traveled given wheel speeds and time.
- **Returns:** Distance traveled in meters

#### `public static double toSecondsForRotation(...)` *(YOU IMPLEMENT)*
Calculates time needed to rotate a specific angle.
- **Returns:** Seconds required for the desired rotation

#### `public static double rotate(...)` *(YOU IMPLEMENT)*
Calculates rotation angle given wheel speeds and time.
- **Returns:** Degrees rotated

### Constants Class (`src/Constants.java`)

#### Motor IDs (you assign these from 1-13)
- `driveBLMotor`, `driveBRMotor`, `driveFLMotor`, `driveFRMotor` - Drive motors
- `intakeMotor` - Intake motor
- `shooterLeftMotor`, `shooterRightMotor` - Shooter motors

#### Physical Constants
- `trackWidthMeters = 1` - Distance between left and right wheels
- `wheelCircumference = π × 2 × 0.05` - Distance per wheel rotation
- `motorMaxSpeed = 30` - Maximum rotations per second
- `allianceColor` - "blue" or "red" (determines starting position)

## Implementation Tips

### For Drive Subsystem
- Think about how wheel speeds relate to robot movement
- Average left and right sides separately
- Consider how differential speeds create rotation

### For Intake/Shooter Subsystems
- Use motor power control (setPower)
- Use Util.sleep() for timed operations
- Remember to turn motors off after operations

### For Autonomous Routine
- Call subsystem methods in sequence
- Track robot position as you move
- Use Util.divider() to make output readable

## Java Utilities You Can Use
Any built-in Java libraries are allowed, including:
- `Math.abs()` - Absolute value
- `Math.signum()` - Sign of a number
- `Math.toRadians()` / `Math.toDegrees()` or `Util.degToRad` / `Util.radToDeg` - Angle conversion
- `System.out.println()` - Printing output

## Getting Started

### Step 1: Assign Motor IDs
Open `src/Constants.java` and assign motor IDs (1-13) to each motor constant.

### Step 2: Implement Util Helper Methods
Open `lib/Util.java` and implement the methods marked for you to complete.

### Step 3: Complete Drive Subsystem
Open `src/subsystems/Drive.java` and implement the movement calculation methods.

### Step 4: Complete Intake Subsystem
Open `src/subsystems/Intake.java` and implement the intake logic.

### Step 5: Complete Shooter Subsystem
Open `src/subsystems/Shooter.java` and implement the shooting logic.

### Step 6: Write Autonomous Routine
Open `src/Robot.java` and write your autonomous routine using the subsystems.

## Testing Your Code
Run your program and verify:
- Motors are assigned valid IDs
- Robot movements produce correct output
- Position tracking is accurate
- Robot stays within field boundaries

## Optional Advanced Features
If you finish early, consider implementing:
- **Swerve Drive** - Individual wheel rotation control (consult interviewer first)
- **Extendable Intake** - Two-motor intake system
- **Elevator Shooter** - Variable height shooting
- **LED Subsystem** - Visual feedback system

## **Good Luck!**
**Remember:** You may look up Java syntax and documentation, but understand what you're writing. Ask interviewers if you need clarification on the robot specifications or constraints.
