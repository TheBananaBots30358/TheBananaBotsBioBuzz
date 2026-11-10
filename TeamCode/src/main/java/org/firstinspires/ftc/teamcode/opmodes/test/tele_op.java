package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Mec BioBuzz StarterBot Teleop", group = "StarterBot")
public class tele_op extends OpMode {

    /*
     * CONTROLS
     * Gamepad 1:
     * Left Stick - Drive / Strafe
     * Right Stick X - Rotate
     */

    private DcMotor leftFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightFrontDrive;
    private DcMotor rightBackDrive;
    private DcMotor intake;
    private DcMotorEx Nectar;
    private DcMotorEx Pollen;

    private Servo nectarGate;
    final double NECTAR_BLOCK_POSITION = 0.0;
    final double NECTAR_RELEASE_POSITION = 0.5;
    private Servo pollenGate;
    final double POLLEN_BLOCK_POSITION = 0.0;
    final double POLLEN_RELEASE_POSITION = 0.5;
    private Servo Flower;
    final double FLOWER_UP_POSITION = 0.0;
    final double FLOWER_INTAKE_POSITION = 0.5;
    double leftFrontPower;
    double rightFrontPower;
    double leftBackPower;
    double rightBackPower;

    @Override
    public void init() {
        // Updated with your exact configuration names
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontRight");
        leftBackDrive = hardwareMap.get(DcMotor.class, "backLeft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backRight");
        intake = hardwareMap.get(DcMotor.class, "intake");
        Nectar = hardwareMap.get(DcMotorEx.class,"Nectar");
        Pollen = hardwareMap.get(DcMotorEx.class,"Pollen");
        nectarGate = hardwareMap.get(Servo.class, "nectarGate");
        pollenGate = hardwareMap.get(Servo.class,"pollenGate");
        Flower = hardwareMap.get(Servo.class, "Flower");


        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);

        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        mecanumDrive(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x
        );

        telemetry.addData(
                "Motors",
                "LF %.2f | RF %.2f",
                leftFrontPower,
                rightFrontPower
        );
    }

    private void mecanumDrive(double forward, double strafe, double rotate) {
        leftFrontPower = forward + strafe + rotate;
        rightFrontPower = forward - strafe - rotate;
        leftBackPower = forward - strafe + rotate;
        rightBackPower = forward + strafe - rotate;

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

        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);

        if (gamepad2.right_bumper) {
            intake.setPower(0.75);
        } else if (gamepad2.left_bumper) {
            intake.setPower(-0.75);
        } else {
            intake.setPower(0);
        }
        if (gamepad2.right_trigger > 0.5) {
            nectarGate.setPosition(NECTAR_RELEASE_POSITION);
            Nectar.setVelocity(2160);
        } else {
            nectarGate.setPosition(NECTAR_BLOCK_POSITION);
            Nectar.setVelocity(2160);
        }

        if (gamepad2.left_trigger > 0.5) {
            pollenGate.setPosition(POLLEN_RELEASE_POSITION);
            Pollen.setVelocity(1860);
        } else {
            pollenGate.setPosition(POLLEN_BLOCK_POSITION);
            Pollen.setVelocity(1860);
        }

        if (gamepad2.dpad_up) {
            Flower.setPosition(FLOWER_INTAKE_POSITION);
        } else {
            Flower.setPosition(FLOWER_UP_POSITION);
        }
    }
}


