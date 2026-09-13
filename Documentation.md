# Class Documentation
> Read Commands.md as well. It was too long to be here

## `public class Motor`

Represents a motor on the robot.

## `public Motor()`

Creates a new Motor.

## `public void setPower(double power)`

Sets the power of the motor.

 * **Parameters:** `power` — The motor power in range [-1, 1] where:

     -1 = full reverse

     0 = stopped

     1 = full forward

## `public double getSpeed()`

Gets the current speed of the motor.

 * **Returns:** The motor speed in rotations per second (RPS).

## `public class FieldPosition`

Represents a position on the field with x, y coordinates and heading.

## `public FieldPosition(double xMeters, double yMeters, double headingDegrees)`

Creates a new FieldPosition.

 * **Parameters:**
   * `xMeters` — X coordinate in meters.
   * `yMeters` — Y coordinate in meters.
   * `headingDegrees` — Heading in degrees.

## `public FieldPosition()`

Creates a new FieldPosition at the origin with 0 degrees heading.

## `public double getX()`

Gets the X coordinate.

## `public double getY()`

Gets the Y coordinate.

## `public double getHeading()`

Gets the heading.

## `public double distanceTo(FieldPosition other)`

Calculates the distance between this FieldPosition and another FieldPosition.

 * **Parameters:** `other` — The other FieldPosition.

     <p>
 * **Returns:** The distance in meters.

## `public double angleTo(FieldPosition other)`

Calculates the angle from this FieldPosition to another FieldPosition.

 * **Parameters:** `other` — The other FieldPosition.

     <p>
 * **Returns:** The angle in degrees.

## `public class Util`

Utility class containing helper functions.

## `public static void sleep(double seconds)`

Pauses execution for a specified duration.

 * **Parameters:** `seconds` — The duration to sleep in seconds (will be clamped 1-135s)

## `public class Constants`

Contains all robot constants.

## `public static final double maxRobotSpeedMetersPerSecond = Motor.maxSpeedRPS * wheelCircumference`

Max robot linear speed in m/s.

## `public static final double maxRobotAngularSpeedDegreesPerSecond = Math.toDegrees(2 * maxRobotSpeedMetersPerSecond`

Max robot angular speed in deg/s.

## `public static final double intakeRunTimeSeconds = 2;`
How long the intake spins for when run.

## `public static final double hopperIndexTimeSeconds = 2;`
How long the hopper spins for when indexing. 

## `public static final double shooterRunTimeSeconds = 4;`
How long the shooter spins for when shooting. 

## `public static final double TICK_SECONDS = 0.02;`
Loop rate for the teleop drive command.
