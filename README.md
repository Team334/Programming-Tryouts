# Java Programming Tryouts

## TODO: Diagram and make this make more sense.

## Overview
You are a programmer on your school's FIRST robotics team, for the CRESCENDO season. The robot has been built and it is your job to
program an autonomous routine for it. However, before you can program the autonomous routine, you must program the individual "subsystems" on the robot before having them work together in a full routine. The robot consists of three subsystems, the **Drive** subsystem, the **Shooter** subsystem, and the **Intake** subsystem. The **Drive** subsystem consists of two Drive wheels, one on the left side of the robot and one on the right side, each powered by a single motor. The **Shooter** subsystem is a fixed-angle shooter on the front of the robot, consisting of multiple flywheels all powered by a single motor. The **Intake** subsystem is on the back of the robot, and is also fixed on the robot, and also consists of several wheels that feed the game piece into the robot, all powered by a single motor. Your goal is to program the autonomous routine described below for the robot.

## Autonomous Routine
The robot is placed directly in front of the goal, pre-loaded with the game piece. The front (where the shooter is) is facing the goal. The autonoumous routine is as follows:
- The robot shoots the piece into the goal.
- The robot drives back 5 meters to a game piece.
- The robot intakes the game piece.
- The robot drives forward 5 meters back to the goal, and shoots the game piece.
- The robot drive forward 5 meters, turns 90 degrees, and then drives 2 meters to another game piece.
- The robot intakes the game piece so it's ready for the teleoperated period.
- The autonomous routine is now complete.

## What You Are Given
- A **Motor** class, which has a function to set its power. The power is a number ranging from [-1, 1], where -1 is full power in the reverse direction, 0 is no power, and 1 is full power in the forward direction. You can also get the speed of the motor, which is its rotations per minute. In this case of your robot, a positive speed on the drive wheels will move the robot forward relative its front, and a negative speed will move the robot backwards relative to its front.

## Programming
- The **Drive** subsystem should be its own class, with private instances of each **Motor**.
- The **Shooter** subsystem should be its own class, with private instances of each **Motor**.
- The **Intake** subsystem should be its own class, with private instaces of each **Motor**.
- Each subsystem class should be defined in the **subsystems** folder.
- Each subsystem should be instantiated in the **Robot** class, and the autonomous routine must be programmed in the **autonomous()**
function of the robot class.

## **Good Luck!**