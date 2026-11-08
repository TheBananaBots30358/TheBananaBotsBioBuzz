package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class intaketest {

    public class Intake {

        private DcMotor intake;

        public Intake(HardwareMap hardwareMap) {
            intake = hardwareMap.get(DcMotor.class, "intake");
        }

        public void update(Gamepad gamepad1) {

            if (gamepad1.right_bumper) {
                intake.setPower(1.0);
            }
            else if (gamepad1.left_bumper) {
                intake.setPower(-1.0);
            }
            else {
                intake.setPower(0);
            }
        }
    }

}
