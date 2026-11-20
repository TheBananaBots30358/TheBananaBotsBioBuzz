package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@Autonomous(name = "Test - Limelight Desk Vision", group = "Test")
public class LimelightDeskTest extends LinearOpMode {

    private Limelight3A limelight;
    private static final double METERS_TO_INCHES = 39.3701;

    // Your configured Limelight pipelines
    private static final int PIPELINE_FINAL  = 0; // IDs: ALL ID

    private static final int PIPELINE_RED_0  = 1; // IDs: 33, 32, 31, 30
    private static final int PIPELINE_RED_1  = 2; // IDs: 34, 35, 36, 37
    private static final int PIPELINE_BLUE_2 = 3; // IDs: 45, 44, 43, 42
    private static final int PIPELINE_BLUE_3 = 4; // IDs: 38, 39, 40, 41

    //private int activePipeline = PIPELINE_RED_0;
    private int activePipeline = PIPELINE_FINAL;


    private static class HiveVisionData {
        boolean detected = false;
        int tagId = -1;
        boolean isUpright = false;
        double strafeInches = 0.0;
        double distanceInches = 0.0;
        double yawDegrees = 0.0;
    }

    @Override
    public void runOpMode() {
        // Initialize ONLY the Limelight (no motors required for desk testing)
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(activePipeline);
        limelight.setPollRateHz(100);
        limelight.start();

        // Init Loop: Switch pipelines with Gamepad 1 and see live telemetry
        while (!isStarted() && !isStopRequested()) {
            if (gamepad1.b) {
                activePipeline = PIPELINE_RED_0;
                limelight.pipelineSwitch(activePipeline);
            } else if (gamepad1.a) {
                activePipeline = PIPELINE_RED_1;
                limelight.pipelineSwitch(activePipeline);
            } else if (gamepad1.x) {
                activePipeline = PIPELINE_BLUE_2;
                limelight.pipelineSwitch(activePipeline);
            } else if (gamepad1.y) {
                activePipeline = PIPELINE_BLUE_3;
                limelight.pipelineSwitch(activePipeline);
            }

            updateVisionTelemetry("INIT PREVIEW");
        }

        waitForStart();

        // Active Loop: Continuously updates on your Driver Station so you can move tags around
        while (opModeIsActive()) {
            updateVisionTelemetry("RUNNING");
        }

        limelight.stop();
    }

    private void updateVisionTelemetry(String mode) {
        HiveVisionData hive = getHiveData();

        telemetry.addData("Mode", mode);
        //telemetry.addLine("B=Red0 | A=Red1 | X=Blue2 | Y=Blue3");
        telemetry.addData("Active Pipeline", getPipelineName(activePipeline));
        telemetry.addLine("--------------------------------");

        if (hive.detected) {
            telemetry.addData("Tag ID Detected", hive.tagId);
            telemetry.addData("Hive Status", hive.isUpright ? "UPRIGHT" : "TIPPED (Upside-Down)");
            telemetry.addData("Distance Z (in)", String.format("%.1f", hive.distanceInches));
            telemetry.addData("Strafe X (in)", String.format("%.1f", hive.strafeInches));
            telemetry.addData("Yaw Angle (deg)", String.format("%.1f", hive.yawDegrees));
        } else {
            telemetry.addLine("No matching AprilTag in view.");
        }

        telemetry.update();
    }

    private String getPipelineName(int index) {
        switch (index) {
            case 0: return "0: ALL (IDs 30-45)";

            case 1: return "1: Red0 (IDs 30-33)";
            case 2: return "2: Red1 (IDs 34-37)";
            case 3: return "3: Blue2 (IDs 42-45)";
            case 4: return "4: Blue3 (IDs 38-41)";
            default: return "Unknown";
        }
    }

    private HiveVisionData getHiveData() {
        HiveVisionData data = new HiveVisionData();
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
            if (!fiducials.isEmpty()) {
                LLResultTypes.FiducialResult tag = fiducials.get(0);
                data.detected = true;
                data.tagId = tag.getFiducialId();

                double tagRollDeg = tag.getTargetPoseCameraSpace().getOrientation().getRoll();
                data.isUpright = Math.abs(tagRollDeg) < 90.0;

                Pose3D robotPose = tag.getRobotPoseTargetSpace();
                data.strafeInches   = robotPose.getPosition().x * METERS_TO_INCHES;
                data.distanceInches = robotPose.getPosition().z * METERS_TO_INCHES;
                data.yawDegrees     = robotPose.getOrientation().getYaw();
            }
        }
        return data;
    }
}