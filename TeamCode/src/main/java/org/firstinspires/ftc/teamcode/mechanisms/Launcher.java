package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ConfigConstants;

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
    private static final double VELOCITY_TOLERANCE = 60;   // "close enough" to shoot

    private final String name;
    private final String motorName;
    private final String gateName;

    private DcMotorEx flywheel;
    private Servo gate;
    private double target_velocity;
    public double gate_open_position;
    public double gate_closed_position;
    ElapsedTime debounce = new ElapsedTime();

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

    public void setFlywheelPID(double P, double I, double D, double F) {
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(P,I,D,F));
    }

    public void init(HardwareMap hwMap) {
        flywheel = hwMap.get(DcMotorEx.class, motorName);
        gate = hwMap.get(Servo.class, gateName);

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        closeGate();
    }

    /** Start the flywheel spinning toward its target speed. */
    public void setVelocity(double velocity) {
        target_velocity = velocity;
        flywheel.setVelocity(target_velocity);
    }

    public void setGatePositions(double open_position, double closed_position){
        gate_open_position = open_position;
        gate_closed_position = closed_position;
    }

    public void stop() {
        flywheel.setVelocity(0);
        closeGate();
    }

    /** True when the flywheel is fast enough to shoot. */
    public boolean isReady() {
        return Math.abs(flywheel.getVelocity() - target_velocity) < VELOCITY_TOLERANCE;
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
        gate.setPosition(gate_open_position);
    }

    public void closeGate() {
        gate.setPosition(gate_closed_position);
    }
    public void tuneGates(boolean button1, boolean button2, Telemetry telemetry){
        telemetry.addData("FloIntPos", gate.getPosition());
        if(button1 && debounce.milliseconds() > 200){
            gate.setPosition(gate.getPosition() + .01);
            debounce.reset();
        } else if (button2 && debounce.milliseconds() > 200) {
            gate.setPosition(gate.getPosition() - .01);
            debounce.reset();
        }
        telemetry.update();
    }
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData(name + " velocity", "%.0f / %.0f", flywheel.getVelocity(), target_velocity);
        telemetry.addData(name + " ready", isReady());
        telemetry.addData(name + " Gate Open Position: "," %.2f", gate_open_position);
        telemetry.addData(name + " Gate Close Position: %.2f",gate_closed_position);
        telemetry.addData(name + " Gate current position: %.2f ",gate.getPosition());
    }
}
