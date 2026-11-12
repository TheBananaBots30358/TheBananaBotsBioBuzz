
package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ConfigConstants;

@Autonomous(name = "Blue_Auto_FrontOfHive")
public class Blue_Auto_FrontOfHive extends OpMode {

    DcMotorEx pollenLauncher;
    Servo pollenGate;
    ElapsedTime timer = new ElapsedTime();

    int step = 0;

    final double TARGET_RPM = 1860.0;
    final double MIN_RPM = 1840.0;
    final double TICKS_PER_REV = 28.0;

    final double GATE_OPEN = 0.5;
    final double GATE_CLOSED = 0.0;

    @Override
    public void init() {

        pollenLauncher = hardwareMap.get(
                DcMotorEx.class,
                ConfigConstants.NAMES.POLLEN_LAUNCHER
        );

        pollenGate = hardwareMap.get(
                Servo.class,
                ConfigConstants.NAMES.POLLEN_GATE
        );

        pollenLauncher.setMode(
                DcMotor.RunMode.RUN_USING_ENCODER
        );

        pollenGate.setPosition(GATE_CLOSED);
    }

    @Override
    public void start() {

        step = 0;
        timer.reset();

        pollenLauncher.setVelocity(
                TARGET_RPM * TICKS_PER_REV / 60.0
        );
    }

    @Override
    public void loop() {

        double rpm =
                Math.abs(pollenLauncher.getVelocity())
                        * 60.0 / TICKS_PER_REV;

        // STEP 0: Wait for at least 1840 RPM
        if (step == 0) {

            if (rpm >= MIN_RPM) {

                pollenGate.setPosition(GATE_OPEN);
                step = 1;
                timer.reset();

            } else if (timer.seconds() >= 5.0) {

                step = 2;
            }
        }

        // STEP 1: Shoot Pollen for 4 seconds
        if (step == 1 && timer.seconds() >= 4.0) {
            step = 2;
        }

        // STEP 2: Close gate, keep launcher running
        if (step == 2) {

            pollenGate.setPosition(GATE_CLOSED);
            step = 3;
        }

        telemetry.addData("Step", step);
        telemetry.addData("RPM", rpm);
        telemetry.update();
    }

    @Override
    public void stop() {

        pollenGate.setPosition(GATE_CLOSED);
        pollenLauncher.setVelocity(0);
    }
}

