
package org.firstinspires.ftc.teamcode.mechanisms;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
@Autonomous
public class pollenPower extends LinearOpMode{
    private DcMotor Pollen;
    public void runOpMode(){
        Pollen = hardwareMap.get(DcMotor.class, "Pollen");
        waitForStart();
        double pollenPower = -2160;
        double powerStep = -0.01;
        while (opModeIsActive()){
            Pollen.setPower(pollenPower);
            telemetry.addData("pollen power:", pollenPower);
            telemetry.update();
            if (gamepad1.a) {
                pollenPower -= powerStep;
            }
            if (gamepad1.b) {
                pollenPower += powerStep;
            }
            telemetry.update();
            sleep(50);
        }
    }
}



