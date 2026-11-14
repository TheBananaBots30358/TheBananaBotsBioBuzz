package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * One launcher: a flywheel motor that spins up to speed, and a gate servo that lets
 * a game piece into the flywheel.
 * OWNER: Launcher team.
 *
 * The robot has two launchers (pollen and nectar). They work the same way, so we use
 * this one class twice with different hardware names. See Robot.java.
 */
public class Launcher {
    // TODO: Tune these numbers on the real robot.
    private static final double TARGET_VELOCITY = 1500;    // encoder ticks per second
    private static final double VELOCITY_TOLERANCE = 50;   // "close enough" to shoot
    private static final double GATE_OPEN_POSITION = 0.5;
    private static final double GATE_CLOSED_POSITION = 0.0;

    private final String name;
    private final String motorName;
    private final String gateName;

    private DcMotorEx flywheel;
    private Servo gate;

    /**
     * @param name      shown on telemetry, e.g. "Pollen"
     * @param motorName flywheel motor name from ConfigConstants
     * @param gateName  gate servo name from ConfigConstants
     */
    public Launcher(String name, String motorName, String gateName) {
        this.name = name;
        this.motorName = motorName;
        this.gateName = gateName;
    }

    public void init(HardwareMap hwMap) {
        flywheel = hwMap.get(DcMotorEx.class, motorName);
        gate = hwMap.get(Servo.class, gateName);

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    /** Start the flywheel spinning toward its target speed. */
    public void spinUp() {
        flywheel.setVelocity(TARGET_VELOCITY);
    }

    public void stop() {
        flywheel.setVelocity(0);
        closeGate();
    }

    /** True when the flywheel is fast enough to shoot. */
    public boolean isReady() {
        return Math.abs(flywheel.getVelocity() - TARGET_VELOCITY) < VELOCITY_TOLERANCE;
    }

    /** Opens the gate only if the flywheel is up to speed. Returns true if it opened. */
    public boolean shoot() {
        if (isReady()) {
            openGate();
            return true;
        }
        return false;
    }

    public void openGate() {
        gate.setPosition(GATE_OPEN_POSITION);
    }

    public void closeGate() {
        gate.setPosition(GATE_CLOSED_POSITION);
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData(name + " velocity", "%.0f / %.0f", flywheel.getVelocity(), TARGET_VELOCITY);
        telemetry.addData(name + " ready", isReady());
    }
}
