package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class DrivingNoStrafe extends BaseTeleOp {
    
    @Override
    public void runOpMode() throws InterruptedException {
        initDrive();
        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            drive(-gamepad1.left_stick_y, gamepad1.right_stick_x);
        }
    }
    public void drive(double forward, double rotate) {
        double leftFrontPower = forward  + rotate;
        double rightFrontPower = forward - rotate;
        double leftBackPower = forward + rotate;
        double rightBackPower = forward - rotate;

        double max = Math.max(
                Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower)),
                Math.max(Math.abs(leftBackPower), Math.abs(rightBackPower))
        );

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        leftFront.setPower(leftFrontPower);
        rightFront.setPower(rightFrontPower);
        leftBack.setPower(leftBackPower);
        rightBack.setPower(rightBackPower);
    }
}
