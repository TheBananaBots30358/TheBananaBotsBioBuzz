/*package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Red Basket Auto", group = "Red")
public class RedBasketAuto extends TwoStageBasketAuto {
    public RedBasketAuto() {
        super(true); // true = Red Alliance
    }
}
*/
package org.firstinspires.ftc.teamcode.limelight;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Auto-Limelightsystem-RED", group = "Auto")
public class RedBasketAuto extends LinearOpMode {

    @Override
    public void runOpMode() {
        LimelightSystem vision = new LimelightSystem(hardwareMap, LimelightSystem.Alliance.RED);
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
