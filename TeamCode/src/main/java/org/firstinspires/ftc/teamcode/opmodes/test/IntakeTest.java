package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;

/**
 * Tests only the intake, so the intake team can work without the other mechanisms.
 *
 * Gamepad 1: A = intake in, B = intake out, Y = flower up / down (press to switch).
 */
@TeleOp(name = "Test: Intake", group = "Test")
public class IntakeTest extends OpMode {
    private final Intake intake = new Intake();

    // Remembers if Y was held last loop, so one press toggles the flower only once
    private boolean flowerButtonWasDown = false;

    @Override
    public void init() {
        intake.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            intake.start();
        } else if (gamepad1.b) {
            intake.reverse();
        } else {
            intake.stop();
        }

        if (gamepad1.y && !flowerButtonWasDown) {
            intake.toggleFlower();
        }
        flowerButtonWasDown = gamepad1.y;

        intake.addTelemetry(telemetry);
    }
}
