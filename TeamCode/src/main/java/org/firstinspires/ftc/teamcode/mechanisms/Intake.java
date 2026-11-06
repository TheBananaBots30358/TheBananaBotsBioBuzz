package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ConfigConstants;

/**
 * Intake: a roller motor that pulls game pieces in, plus the flower servo.
 * OWNER: Intake team.
 */
public class Intake {
    // TODO: Tune these numbers on the real robot.
    private static final double INTAKE_POWER = 1.0;
    private static final double FLOWER_UP_POSITION = 0.5;
    private static final double FLOWER_DOWN_POSITION = 0.0;

    private DcMotor intakeMotor;
    private Servo flower;
    private boolean flowerIsUp;

    public void init(HardwareMap hwMap) {
        intakeMotor = hwMap.get(DcMotor.class, ConfigConstants.NAMES.INTAKE_MOTOR);
        flower = hwMap.get(Servo.class, ConfigConstants.NAMES.FLOWER_INTAKE);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lowerFlower();
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
        flower.setPosition(FLOWER_UP_POSITION);
        flowerIsUp = true;
    }

    public void lowerFlower() {
        flower.setPosition(FLOWER_DOWN_POSITION);
        flowerIsUp = false;
    }

    /** If the flower is up, lower it. If it is down, raise it. */
    public void toggleFlower() {
        if (flowerIsUp) {
            lowerFlower();
        } else {
            raiseFlower();
        }
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Intake power", "%.2f", intakeMotor.getPower());
        telemetry.addData("Flower", flowerIsUp ? "UP" : "DOWN");
    }
}
