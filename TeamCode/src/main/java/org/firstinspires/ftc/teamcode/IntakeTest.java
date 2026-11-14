package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;

public class IntakeTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        Intake intake = new Intake();
        intake.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            if (gamepad1.right_bumper) {
                intake.reverse();
            }
            else if (gamepad1.left_bumper) {
                intake.start();
            }
            else {
                intake.stop();
            }
        }
    }
}
