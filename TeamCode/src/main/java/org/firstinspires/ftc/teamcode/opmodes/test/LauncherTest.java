package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ConfigConstants;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;

/**
 * Tests only the two launchers, so the launcher team can work without the other mechanisms.
 *
 * Gamepad 1:
 *   right bumper  spin up pollen (hold), A = shoot pollen
 *   left bumper   spin up nectar (hold), B = shoot nectar
 */
@TeleOp(name = "Test: Launcher", group = "Test")
public class LauncherTest extends OpMode {
    private final Launcher pollen = new Launcher("Pollen",
            ConfigConstants.NAMES.POLLEN_LAUNCHER, ConfigConstants.NAMES.POLLEN_GATE);
    private final Launcher nectar = new Launcher("Nectar",
            ConfigConstants.NAMES.NECTAR_LAUNCHER, ConfigConstants.NAMES.NECTAR_GATE);

    @Override
    public void init() {
        pollen.init(hardwareMap);
        nectar.init(hardwareMap);
    }

    @Override
    public void loop() {
        testLauncher(pollen, gamepad1.a);
        testLauncher(nectar, gamepad1.b);

        pollen.addTelemetry(telemetry);
        nectar.addTelemetry(telemetry);
    }

    private void testLauncher(Launcher launcher, boolean shootButton) {
        if (shootButton) {
            launcher.shoot();
        } else {
            launcher.closeGate();
        }
    }
}
