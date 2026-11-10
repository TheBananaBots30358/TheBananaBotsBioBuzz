package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class BaseTeleOp extends LinearOpMode {
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor leftBack;
    public DcMotor rightBack;
    public DcMotor intake;
    public DcMotor pollenLauncher;
    public DcMotor nectarLauncher;
    public Servo flowerServo;
    public Servo nectarGate;
    public Servo pollenGate;
    public GoBildaPinpointDriver pinpoint;

    public void initHardware() {
        leftFront = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.FRONT_LEFT_DRIVE_MOTOR);
        rightFront = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.FRONT_RIGHT_DRIVE_MOTOR);
        leftBack = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.BACK_LEFT_DRIVE_MOTOR);
        rightBack = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.BACK_RIGHT_DRIVE_MOTOR);
        intake = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.INTAKE_MOTOR);
        pollenLauncher = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.POLLEN_LAUNCHER);
        nectarLauncher = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.NECTAR_LAUNCHER);
        flowerServo = hardwareMap.get(Servo.class, ConfigConstants.NAMES.FLOWER_INTAKE);
        nectarGate = hardwareMap.get(Servo.class, ConfigConstants.NAMES.NECTAR_GATE);
        pollenGate = hardwareMap.get(Servo.class, ConfigConstants.NAMES.POLLEN_GATE);
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, ConfigConstants.NAMES.ODOMETRY_COMPUTER);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void initDrive() {
        leftFront = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.FRONT_LEFT_DRIVE_MOTOR);
        rightFront = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.FRONT_RIGHT_DRIVE_MOTOR);
        leftBack = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.BACK_LEFT_DRIVE_MOTOR);
        rightBack = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.BACK_RIGHT_DRIVE_MOTOR);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void initIntake() {
        intake = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.INTAKE_MOTOR);
    }
    public void initLaunchers() {
        pollenLauncher = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.POLLEN_LAUNCHER);
        nectarLauncher = hardwareMap.get(DcMotor.class, ConfigConstants.NAMES.NECTAR_LAUNCHER);
    }
    public void initGates() {
        nectarGate = hardwareMap.get(Servo.class, ConfigConstants.NAMES.NECTAR_GATE);
        pollenGate = hardwareMap.get(Servo.class, ConfigConstants.NAMES.POLLEN_GATE);
    }
    public void initFlower() {
        flowerServo = hardwareMap.get(Servo.class, ConfigConstants.NAMES.FLOWER_INTAKE);
    }
    public void initPinpoint() {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, ConfigConstants.NAMES.ODOMETRY_COMPUTER);
    }
}
