package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Intake", group = "intake")

public class IntakeCode extends OpMode {
    private DcMotor Intake;

    @Override
    public void init() {
        Intake = hardwareMap.get(DcMotor.class, "intake");
    }

    @Override
    public void loop() {
        if (gamepad2.left_trigger > 0.1) {
            Intake.setPower(-1);
        }
        else {
            Intake.setPower(0);
        }
        if (gamepad2.right_trigger > 0.1){
            Intake.setPower(1);
        }
        else {
    Intake.setPower(0);
        }
}
}