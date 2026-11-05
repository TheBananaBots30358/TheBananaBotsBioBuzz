package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;

/**
 * Driver-controlled period.
 *
 * Gamepad 1 (driver):
 *   left stick      drive and strafe
 *   right stick X   turn
 *   left bumper     slow mode (hold)
 *
 * Gamepad 2 (operator):
 *   right trigger   intake in (hold)
 *   left trigger    intake out (hold)
 *   Y               flower up / down (press to switch)
 *   right bumper    spin up pollen launcher (hold), then press A to shoot
 *   left bumper     spin up nectar launcher (hold), then press B to shoot
 */
@TeleOp(name = "TeleOp Main", group = "Competition")
public class TeleOpMain extends OpMode {
    // Slow mode: while the driver holds left bumper, drive speed is multiplied by this.
    // 0.4 means 40% speed, for lining up precisely. Lower = slower, higher = faster (max 1.0).
    private static final double SLOW_MODE_SCALE = 0.4;

    private final Robot robot = new Robot();

    // Remembers if Y was held last loop, so one press toggles the flower only once
    private boolean flowerButtonWasDown = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addLine("Ready");
    }

    @Override
    public void loop() {
        driveControls();
        intakeControls();
        launcherControls(robot.pollenLauncher, gamepad2.right_bumper, gamepad2.a);
        launcherControls(robot.nectarLauncher, gamepad2.left_bumper, gamepad2.b);

        robot.addTelemetry(telemetry);
    }

    @Override
    public void stop() {
        robot.stopAll();
    }

    private void driveControls() {
        double forward = -gamepad1.left_stick_y;
        double right = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        if (gamepad1.left_bumper) {
            forward *= SLOW_MODE_SCALE;
            right *= SLOW_MODE_SCALE;
            rotate *= SLOW_MODE_SCALE;
        }
        robot.drivetrain.drive(forward, right, rotate);
    }

    private void intakeControls() {
        if (gamepad2.right_trigger > 0.5) {
            robot.intake.start();
        } else if (gamepad2.left_trigger > 0.5) {
            robot.intake.reverse();
        } else {
            robot.intake.stop();
        }

        if (gamepad2.y && !flowerButtonWasDown) {
            robot.intake.toggleFlower();
        }
        flowerButtonWasDown = gamepad2.y;
    }

    private void launcherControls(Launcher launcher, boolean spinButton, boolean shootButton) {
        if (spinButton) {
            launcher.spinUp();
            if (shootButton) {
                launcher.shoot();
            } else {
                launcher.closeGate();
            }
        } else {
            launcher.stop();
        }
    }
}
