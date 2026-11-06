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
            telemetry.addLine("On gamepad 2, to increase Nectar velocity press A, to decrease velocity press B, to increase P value press Y, to decrease P value press X, to increase I value press up, to decrease I value press down. Hold right trigger to close nectar gate and release to open.");
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
                nectarGate.setPosition(0.65);
            } else {
                nectarGate.setPosition(0.57); // reset servo when you let go
            }

            Nectar.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(NectarP, NectarI, 0, 0));
            Nectar.setVelocity(NectarVelocity);
            Pollen.setPower(PollenPower);
            telemetry.update();
            sleep(100);
        }
    }
}

