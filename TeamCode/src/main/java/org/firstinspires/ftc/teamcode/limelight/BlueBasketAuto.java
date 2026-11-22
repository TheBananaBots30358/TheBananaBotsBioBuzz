package org.firstinspires.ftc.teamcode.limelight;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Auto-Limelightsystem-BLUE", group = "Auto")
public class BlueBasketAuto extends LinearOpMode {

    @Override
    public void runOpMode() {
        LimelightSystem vision = new LimelightSystem(hardwareMap, LimelightSystem.Alliance.BLUE);
        vision.start();

        // Preview telemetry during Init
        while (!isStarted() && !isStopRequested()) {
            LimelightSystem.HiveVisionData hive = vision.getHiveData();
            vision.addTelemetry(telemetry, hive);
            telemetry.update();
        }

        if (opModeIsActive()) {
            LimelightSystem.HiveVisionData hive = vision.getHiveData();

            if (hive.detected) {
                if (hive.isTipped) {
                    // Run tipped hive logic using hive.xInches, hive.zInches, hive.yawDegrees
                } else {
                    // Run upright hive logic using hive.xInches, hive.zInches, hive.yawDegrees
                }
            }
        }

        vision.stop();
    }
}
