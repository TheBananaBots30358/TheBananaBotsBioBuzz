# Banana Bots Team Code Guide

How our robot code is organized, and how sub-teams work on it at the same time.

## Where things live

All our code is in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`:

```
ConfigConstants.java     Hardware names. Must match the robot configuration on the Control Hub.
Robot.java               Creates every mechanism. TeleOp and Auto both use it.
mechanisms/
  Drivetrain.java        Drivetrain team
  Intake.java            Intake team
  Launcher.java          Launcher team (used twice: pollen and nectar)
opmodes/
  TeleOpMain.java        Driver-controlled period
  AutoMain.java          Autonomous period (state machine + Pedro Pathing)
  test/
    DrivetrainTest.java  Test the drivetrain by itself
    IntakeTest.java      Test the intake by itself
    LauncherTest.java    Test the launchers by themselves
pedro/                   Pedro Pathing setup and tuners (used by Autonomous)
```

## The rules

1. **Each team edits its own mechanism file.** The intake team edits `Intake.java`,
   the launcher team edits `Launcher.java`, and so on. If two teams don't edit the
   same file, git can combine their work without conflicts.

2. **OpModes never touch motors or servos directly.** They call mechanism methods
   like `robot.intake.start()` or `robot.pollenLauncher.shoot()`. That way a team can
   change how its mechanism works inside without breaking TeleOp or Auto.

3. **Don't rename or remove a public method without telling the other teams.**
   Public methods like `start()`, `spinUp()` and `isReady()` are a promise to everyone
   else. Adding new methods is fine.

4. **Hardware names only go in `ConfigConstants.java`.** Never type a name like
   `"intake"` anywhere else. If a name changes on the Control Hub, change it in that
   one file.

5. **Numbers to tune go at the top of the mechanism file** as `private static final`
   constants, such as `TARGET_VELOCITY`. Look for `TODO` comments to find them.

## Adding something new

- **A new method on your mechanism** (for example `intake.isFull()`): add it to your
  mechanism file, then try it in your test OpMode.
- **A brand-new mechanism**: create `mechanisms/YourThing.java` with an
  `init(HardwareMap)` method, add its hardware names to `ConfigConstants.java`, add it
  to `Robot.java` (in `init`, `stopAll` and `addTelemetry`), and make a test OpMode for it.
- **Using it in a match**: ask whoever owns `TeleOpMain` or `AutoMain` to add the
  button or the Auto step, or add it yourself in a small pull request.

## Testing your part

On the Driver Station, pick your test OpMode from the TeleOp list:
`Test: Drivetrain`, `Test: Intake` or `Test: Launcher`. They only use your mechanism,
so you can test even if another team's part isn't finished or plugged in.

## Git workflow

1. Get the latest code: switch to `master` and pull.
2. Make a branch for your work, for example `intake-sensor`.
3. Make your change and build it. On a Mac, run `./build-debug.sh`, or use
   Build > Make Project in Android Studio.
4. Test it on the robot with your test OpMode.
5. Commit, push your branch, and open a pull request into `master`.
6. Someone else from  team reviews it before it's merged.

Keep pull requests small: one feature or fix each. Small changes are easier to review
and less likely to conflict.

## Autonomous

`AutoMain` is a **state machine**: a list of steps (`DRIVE_TO_LAUNCH`, `SPIN_UP`,
`SHOOT`, ...). Each time `loop()` runs, it checks the current step and decides whether
to move to the next one. To add a step, add a name to the `State` enum and a matching
`case` in the `switch`.

Pedro Pathing drives the robot in Auto. Until the Pedro tuners have been run and
`pedro/Constants.java` is filled in, Auto shows a message and does nothing.
