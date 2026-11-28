package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class GatesTuner extends LinearOpMode {
    private Servo gate;
    @Override
    public void runOpMode(){
        gate = hardwareMap.get(Servo.class, ConfigConstants.NAMES.NECTAR_GATE);
        double openPos = 0;
        double closePos = 1;
        ElapsedTime debounce = new ElapsedTime();
        waitForStart();
        while (opModeIsActive()){
            if (gamepad1.left_bumper && debounce.milliseconds() >= 200){
                openPos += 0.01;
                openPos = Math.max(Math.min(openPos, 1), 0);
                debounce.reset();
            }
            if (gamepad1.right_bumper){
                openPos -= 0.01;
                openPos = Math.max(Math.min(openPos, 1), 0);
                debounce.reset();
            }
            if (gamepad2.left_bumper && debounce.milliseconds() >= 200){
                closePos += 0.01;
                closePos = Math.max(Math.min(closePos, 1), 0);
                debounce.reset();
            }
            if (gamepad2.right_bumper){
                closePos -= 0.01;
                closePos = Math.max(Math.min(closePos, 1), 0);
                debounce.reset();
            }
            if (gamepad1.a){
                gate.setPosition(openPos);
            } else {
                gate.setPosition(closePos);
            }

            telemetry.addData("Open", openPos);
            telemetry.addData("Closed", closePos);
            telemetry.update();
        }
    }
}
