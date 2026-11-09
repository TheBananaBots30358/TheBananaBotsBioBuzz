
package org.firstinspires.ftc.teamcode.mechanisms;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous


public class IntakePollen extends LinearOpMode{
    private DcMotorEx Pollen;
    private double PollenP = 20;
    private double PollenI = 0.2;
    private DcMotor intake;
    public void runOpMode(){
        Pollen = hardwareMap.get(DcMotorEx.class, "Pollen");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        Pollen.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(PollenP,PollenI,0,0));
        waitForStart();
        double PollenVelocity = -1860;


        while (opModeIsActive()){
            Pollen.setVelocity(PollenVelocity);
            intake.setPower(-0.7);
            telemetry.addData("pollen set velocity:", PollenVelocity);
            telemetry.addData("Pollen encoder velocity", Math.abs(Pollen.getVelocity()));
            telemetry.addData("pollen P:", PollenP);
            telemetry.addData("pollen I:", PollenI);
            telemetry.update();
            Pollen.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(PollenP, PollenI, 0, 0));
            Pollen.setVelocity(PollenVelocity);
            telemetry.update();
            sleep(100);
        }
    }
}



