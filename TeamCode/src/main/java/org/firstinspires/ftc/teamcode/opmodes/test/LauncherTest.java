package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ConfigConstants;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;

/**
 * Tests only the two launchers, so the launcher team can work without the other mechanisms.
 *
 * Gamepad 1:
 *    A = shoot pollen
 *    B = shoot nectar
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
        pollen.setFlywheelPID(20,0.2,0,0);
        pollen.setVelocity(-1860);
        pollen.setGatePositions(0.5,0);

        nectar.init(hardwareMap);
        nectar.setFlywheelPID(20,0.2,0,0);
        nectar.setVelocity(2160);
        nectar.setGatePositions(0.57, 0.65);
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
            launcher.openGate();
        } else {
            launcher.closeGate();
        }
    }
}
