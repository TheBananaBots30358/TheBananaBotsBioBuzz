package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class FloIntCoding extends LinearOpMode {
    Servo FloInt;

    @Override
    public void runOpMode() throws InterruptedException {
        FloInt = hardwareMap.get(Servo.class, "FloInt");
        telemetry.addData("Servo Position: ", FloInt.getPosition());

        if (gamepad1.left_trigger_pressed){
            FloInt.setPosition(0);
        }
        if (gamepad1.left_trigger_pressed){
            FloInt.setPosition(.25);
        }
        if (gamepad1.left_trigger_pressed){
            FloInt.setPosition(.5);
        }
    }
}