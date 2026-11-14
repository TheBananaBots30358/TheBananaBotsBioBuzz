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
        robot.nectarLauncher.setVelocity(2160);
        robot.pollenLauncher.setVelocity(-1940);
        driveControls();
        intakeControls();
        launcherControls(robot.pollenLauncher, gamepad2.a);
        launcherControls(robot.nectarLauncher, gamepad2.b);
      

        robot.addTelemetry(telemetry);
    }

    @Override
    public void stop() {
        robot.stopAll();
    }

    private void driveControls() {
        double forward = 0.6666666666666666 * Math.pow(-gamepad1.left_stick_y, 3) + 0.33333333333333 * -gamepad1.left_stick_y;
        double right = 0.6666666666666666 * Math.pow(gamepad1.left_stick_x, 3) + 0.33333333333333 * gamepad1.left_stick_x;
        double rotate = 0.6666666666666666 * Math.pow(gamepad1.right_stick_x, 3) + 0.33333333333333 * gamepad1.right_stick_x;

        if (gamepad1.left_bumper) {
            forward *= SLOW_MODE_SCALE;
            right *= SLOW_MODE_SCALE;
            rotate *= SLOW_MODE_SCALE;
        }

        robot.drivetrain.drive(forward, right, rotate);
    }

    private void intakeControls() {
        //INTAKE
        if (gamepad2.right_trigger > 0.5) {
            robot.intake.start();
        } else if (gamepad2.left_trigger > 0.5) {
            robot.intake.reverse();
        } else {
            robot.intake.stop();
        }
        //FLOINT
        if (gamepad2.y && !flowerButtonWasDown) {
            robot.intake.toggleFlower();
        }
        flowerButtonWasDown = gamepad2.y;

        robot.intake.tuneFloInt(gamepad2.dpad_up, gamepad2.dpad_down, telemetry);
    }

    private void launcherControls(Launcher launcher, boolean shootButton) {
        if (shootButton) {
            launcher.shoot();
        } else {
            launcher.closeGate();
        }
    }
    private void openGate(Launcher launcher, boolean shootButton) {
        if (shootButton) {
            launcher.shoot();
        } else {
            launcher.closeGate();
        }
    }
}
