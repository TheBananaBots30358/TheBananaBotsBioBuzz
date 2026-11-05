package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;

/**
 * Tests only the drivetrain, so the drivetrain team can work without the other mechanisms.
 *
 * Gamepad 1: left stick drives and strafes, right stick X turns.
 */
@TeleOp(name = "Test: Drivetrain", group = "Test")
public class DrivetrainTest extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();

    @Override
    public void init() {
        drivetrain.init(hardwareMap);
    }

    @Override
    public void loop() {
        drivetrain.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        drivetrain.addTelemetry(telemetry);
    }
}
