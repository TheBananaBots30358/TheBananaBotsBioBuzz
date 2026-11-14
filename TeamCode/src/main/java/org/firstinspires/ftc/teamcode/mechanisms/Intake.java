package org.firstinspires.ftc.teamcode.mechanisms;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ConfigConstants;


/**
 * Intake: a roller motor that pulls game pieces in, plus the FloInt servo.
 * OWNER: Intake team.
 */
public class Intake {
    // TODO: Tune these numbers on the real robot.

    ElapsedTime debounce = new ElapsedTime();
    private static final double INTAKE_POWER = 0.8;
    private static final double FLOWER_UP_POSITION = 0.39;
    private static final double FLOWER_DOWN_POSITION = 0.13;

    private DcMotor intakeMotor;
    private Servo FloInt;
    private boolean flowerIsUp;

    public void init(HardwareMap hwMap) {
        intakeMotor = hwMap.get(DcMotor.class, ConfigConstants.NAMES.INTAKE_MOTOR);
        FloInt = hwMap.get(Servo.class, ConfigConstants.NAMES.FLOWER_INTAKE);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FloInt.setPosition(.42);
    }

    /** Spin the rollers to pull pieces in. */
    public void start() {
        intakeMotor.setPower(INTAKE_POWER);
    }

    /** Spin the rollers backward to spit pieces out. */
    public void reverse() {
        intakeMotor.setPower(-INTAKE_POWER);
    }

    public void stop() {
        intakeMotor.setPower(0);
    }

    public void raiseFlower() {
        FloInt.setPosition(FLOWER_UP_POSITION);
        flowerIsUp = true;
    }

    public void lowerFlower() {
        FloInt.setPosition(FLOWER_DOWN_POSITION);
        flowerIsUp = false;
    }

    /** If the FloInt is up, lower it. If it is down, raise it. */
    public void toggleFlower() {
        if (flowerIsUp) {
            lowerFlower();
        } else {
            raiseFlower();
        }
    }
    public void tuneFloInt(boolean button1, boolean button2, Telemetry telemetry){
        telemetry.addData("FloIntPos", FloInt.getPosition());
        if(button1 && debounce.milliseconds() > 200){
            FloInt.setPosition(FloInt.getPosition() + .01);
            debounce.reset();
        } else if (button2 && debounce.milliseconds() > 200) {
            FloInt.setPosition(FloInt.getPosition() - .01);
            debounce.reset();
        }
        telemetry.update();
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Intake power", "%.2f", intakeMotor.getPower());
        telemetry.addData("Flower", flowerIsUp ? "UP" : "DOWN");
    }
}
