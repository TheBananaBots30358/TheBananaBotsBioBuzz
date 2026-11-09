

package org.firstinspires.ftc.teamcode.mechanisms;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;




@Autonomous


public class LauncherWithPID extends LinearOpMode{
    private DcMotorEx Nectar;
    private Servo nectarGate;
    private DcMotorEx Pollen;
    private Servo pollenGate;
    private double NectarP = 20;
    private double NectarI = 0.2;
    private double NectarP_INCREMENT = 1;
    private double NectarI_INCREMENT =0.01;
    private double PollenP = 20;
    private double PollenI = 0.2;
    private double PollenP_INCREMENT = 1;
    private double PollenI_INCREMENT =0.01;
    public void runOpMode(){
        Nectar = hardwareMap.get(DcMotorEx.class, "Nectar");
        Pollen = hardwareMap.get(DcMotorEx.class, "Pollen");
        nectarGate = hardwareMap.get(Servo.class, "nectarGate");
        Nectar.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(NectarP,NectarI,0,0));
        Pollen.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(PollenP,PollenI,0,0));
        waitForStart();
        double NectarVelocity = 2160;
        double velocity_stepNectar = 20;
        double PollenVelocity = -1860;
        double velocity_stepPollen = -1;




        while (opModeIsActive()){




            double currentPosition = nectarGate.getPosition();

// power
            Nectar.setVelocity(NectarVelocity);
            Pollen.setPower(PollenVelocity);
            telemetry.addLine("On gamepad 1(pollen) and 2(nectar), to increase velocity press A, to decrease velocity press B, to increase P value press Y, to decrease P value press X, to increase I value press up, to decrease I value press down. Hold right trigger to open nectar gate and release to close.");
            telemetry.addData("nectar set velocity:", NectarVelocity);
            telemetry.addData("pollen set velocity:", PollenVelocity);
            telemetry.addData("Nectar encoder velocity", Math.abs(Nectar.getVelocity()));
            telemetry.addData("Pollen encoder velocity", Math.abs(Pollen.getVelocity()));
            telemetry.addData("nectar P:", NectarP);
            telemetry.addData("nectar I:", NectarI);
            telemetry.addData("pollen P:", PollenP);
            telemetry.addData("pollen I:", PollenI);
            telemetry.addData("Servo gate position: ",currentPosition);








            telemetry.update();
            if (gamepad1.a) {
                PollenVelocity -= velocity_stepPollen;
            }
            if (gamepad1.b) {
                PollenVelocity += velocity_stepPollen;
            }
            if (gamepad2.a) {
                NectarVelocity += velocity_stepNectar;
            }
            if (gamepad2.b) {
                NectarVelocity -= velocity_stepNectar;
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
            if (gamepad1.x) {
                PollenP -= PollenP_INCREMENT;
            }
            if (gamepad1.y) {
                PollenP += PollenP_INCREMENT;
            }
            if (gamepad1.dpad_up) {
                PollenI += PollenI_INCREMENT;
            }
            if (gamepad1.dpad_down) {
                PollenI -= PollenI_INCREMENT;
            }


            Nectar.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(NectarP, NectarI, 0, 0));
            Nectar.setVelocity(NectarVelocity);
            Pollen.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(PollenP, PollenI, 0, 0));
            Pollen.setVelocity(PollenVelocity);
            telemetry.update();
            sleep(100);
        }
    }
}