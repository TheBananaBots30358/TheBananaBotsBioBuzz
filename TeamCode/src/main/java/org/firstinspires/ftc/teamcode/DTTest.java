package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp
public class DTTest extends LinearOpMode {
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    @Override
    public void runOpMode() throws InterruptedException {
         leftFront = hardwareMap.get(DcMotor.class, "frontLeft");
         rightFront = hardwareMap.get(DcMotor.class, "frontRight");
         leftBack = hardwareMap.get(DcMotor.class, "backLeft");
         rightBack = hardwareMap.get(DcMotor.class, "backRight");
         leftFront.setDirection(DcMotor.Direction.REVERSE);
         leftBack.setDirection(DcMotor.Direction.REVERSE);
         telemetry.addLine("Ready");
         telemetry.update();
         waitForStart();
         while (opModeIsActive()) {
             drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
         }
    }
    public void drive(double forward, double strafe, double rotate) {
         double leftFrontPower = forward + strafe + rotate;
         double rightFrontPower = forward - strafe - rotate;
         double leftBackPower = forward - strafe + rotate;
         double rightBackPower = forward + strafe - rotate;

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
