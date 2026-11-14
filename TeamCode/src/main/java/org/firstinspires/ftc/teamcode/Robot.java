package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;

/**
 * The whole robot in one place. TeleOp and Autonomous both use this class,
 * so every OpMode sets up the hardware the same way.
 *
 * When a team adds a new mechanism, add it here.
 */
public class Robot {
    public final Drivetrain drivetrain = new Drivetrain();
    public final Intake intake = new Intake();
    public final Launcher pollenLauncher = new Launcher("Pollen",
            ConfigConstants.NAMES.POLLEN_LAUNCHER, ConfigConstants.NAMES.POLLEN_GATE);
    public final Launcher nectarLauncher = new Launcher("Nectar",
            ConfigConstants.NAMES.NECTAR_LAUNCHER, ConfigConstants.NAMES.NECTAR_GATE);

    public void init(HardwareMap hwMap) {
        drivetrain.init(hwMap);
        intake.init(hwMap);

        // Set gate positions before init(): init() closes the gate, so it needs
        // the real closed position first. Otherwise the gate goes to 0.0.
        pollenLauncher.setGatePositions(0.48,0.45);
        pollenLauncher.init(hwMap);
        pollenLauncher.setFlywheelPID(100,0.05,0,12.15);

        nectarLauncher.setGatePositions(0, 0.5);
        nectarLauncher.init(hwMap);
        nectarLauncher.setFlywheelPID(100,0.05,0,12.15);
    }

    public void stopAll() {
        drivetrain.stop();
        intake.stop();
        pollenLauncher.stop();
        nectarLauncher.stop();
    }

    public void addTelemetry(Telemetry telemetry) {
        drivetrain.addTelemetry(telemetry);
        intake.addTelemetry(telemetry);
        pollenLauncher.addTelemetry(telemetry);
        nectarLauncher.addTelemetry(telemetry);
    }
}
