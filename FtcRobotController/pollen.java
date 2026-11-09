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
        closeGate();
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




package org.firstinspires.ftc.teamcode.mechanisms;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous


public class Launcher extends LinearOpMode{
    private DcMotorEx Nectar;
    private Servo nectarGate;
    private DcMotor Pollen;
    private double NectarP = 20;
    private double NectarI = 0.2;
    private double NectarP_INCREMENT = 1;
    private double NectarI_INCREMENT =0.01;
    public void runOpMode(){
        Nectar = hardwareMap.get(DcMotorEx.class, "Nectar");
        Pollen = hardwareMap.get(DcMotor.class, "Pollen");
        nectarGate = hardwareMap.get(Servo.class, "nectarGate");
        Nectar.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(NectarP,NectarI,0,0));
        waitForStart();
        double NectarVelocity = 2160;
        double PollenPower = -0.7;
        double power_step = 0.01;
        double velocity_step = 20;


        while (opModeIsActive()){


            double currentPosition = nectarGate.getPosition();
            // power
            Nectar.setVelocity(NectarVelocity);
            Pollen.setPower(PollenPower);
            telemetry.addLine("On gamepad 2, to increase Nectar velocity press A, to decrease velocity press B, to increase P value press Y, to decrease P value press X, to increase I value press up, to decrease I value press down. Hold right trigger to open nectar gate and release to close.");
            telemetry.addData("nectar set velocity:", NectarVelocity);
            telemetry.addData("pollen power:", PollenPower);
            telemetry.addData("Nectar encoder velocity", Math.abs(Nectar.getVelocity()));
            telemetry.addData("nectar P:", NectarP);
            telemetry.addData("nectar I:", NectarI);
            telemetry.addData("Servo gate position: ",currentPosition);




            telemetry.update();
            if (gamepad1.a) {
                PollenPower -= power_step;
            }
            if (gamepad1.b) {
                PollenPower += power_step;
            }
            if (gamepad2.a) {
                NectarVelocity += velocity_step;
            }
            if (gamepad2.b) {
                NectarVelocity -= velocity_step;
            }
            if (gamepad2.x) {
                NectarP -= NectarP_INCREMENT;
            }
            if (gamepad2.y) {
                NectarP += NectarP_INCREMENT;
            }
            if (gamepad2.dpad_up) {
                NectarI += NectarI_INCREMENT;
            }
            if (gamepad2.dpad_down) {
                NectarI -= NectarI_INCREMENT;
            }
            if (gamepad2.right_trigger_pressed) {
                nectarGate.setPosition(0.57);
            } else {
                nectarGate.setPosition(0.65);
            }


            Nectar.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(NectarP, NectarI, 0, 0));
            Nectar.setVelocity(NectarVelocity);
            Pollen.setPower(PollenPower);
            telemetry.update();
            sleep(100);
        }
    }
}



