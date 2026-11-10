package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name="intake with base")
public class Intake extends BaseTeleOp {
    public void runOpMode() {
    initIntake();
    waitForStart();
        while (opModeIsActive()) {
            intake.setPower(-0.5);
            if (gamepad1.a) {
                intake.setDirection(DcMotor.Direction.REVERSE);
            }
        }
    }
}
