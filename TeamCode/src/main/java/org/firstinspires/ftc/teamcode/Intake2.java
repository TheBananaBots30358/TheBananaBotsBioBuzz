package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name="intake without base")
public class Intake2 extends BaseTeleOp {
    public void runOpMode() {
    DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
    waitForStart();
        while (opModeIsActive()) {
            intake.setPower(-1);
            if (gamepad1.a) {
                intake.setDirection(DcMotor.Direction.REVERSE);
            }
        }
    }
}
