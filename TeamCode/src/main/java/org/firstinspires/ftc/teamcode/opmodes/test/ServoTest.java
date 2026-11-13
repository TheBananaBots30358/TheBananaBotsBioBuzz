package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ConfigConstants;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
@TeleOp
public class ServoTest extends OpMode {
    Servo pgate;
    double pos = 0;
    ElapsedTime debounce = new ElapsedTime();
    @Override
    public void init() {
        pgate = hardwareMap.get(Servo.class, "pollenGate");
    }

    @Override
    public void loop() {
        telemetry.addData("pos of pgate", pgate.getPosition());

        pgate.setPosition(pos);

        if (gamepad1.dpad_up && debounce.milliseconds() > 200){
            pos += .01;
            debounce.reset();
        } else if (gamepad1.dpad_down && debounce.milliseconds() > 200){
            pos -= .01;
            debounce.reset();
        }
        telemetry.update();
    }
}