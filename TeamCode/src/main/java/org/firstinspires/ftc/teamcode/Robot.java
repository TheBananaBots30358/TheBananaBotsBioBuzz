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
        pollenLauncher.init(hwMap);
        nectarLauncher.init(hwMap);
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
