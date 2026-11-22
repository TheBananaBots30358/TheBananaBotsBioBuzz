package org.firstinspires.ftc.teamcode.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@Autonomous(name = "LimeLight Test", group = "Test")
public class LimeLightTest extends LinearOpMode {

    public enum Alliance {
        RED, BLUE, ALL
    }

    private Limelight3A limelight;
    private Alliance targetAlliance = Alliance.ALL;

    // Constants matching LimelightSystem
    private static final double METERS_TO_INCHES = 39.3701;
    private static final int PIPELINE_ALL_TAGS = 0;

    private static final int RED_TAG_MIN = 30;
    private static final int RED_TAG_MAX = 37;
    private static final int BLUE_TAG_MIN = 38;
    private static final int BLUE_TAG_MAX = 45;

    private static final double TIPPED_ROLL_THRESHOLD_DEG = 90.0;

    // Standalone test data container matching LimelightSystem.HiveVisionData
    public static class HiveVisionData {
        public boolean detected = false;
        public int tagId = -1;
        public String hiveColor = "NONE";
        public boolean isTipped = false;
        public boolean isUpright = false;
        public double xInches = 0.0;    // Strafe (Left/Right)
        public double yInches = 0.0;    // Vertical (Up/Down)
        public double zInches = 0.0;    // Distance (Forward/Back)
        public double yawDegrees = 0.0; // Heading angle relative to the tag
    }

    @Override
    public void runOpMode() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(PIPELINE_ALL_TAGS);
        limelight.setPollRateHz(100);
        limelight.start();

        // Live preview during Init (use gamepad1 buttons to test alliance filtering)
        //This is for testing
        while (!isStarted() && !isStopRequested()) {
            if (gamepad1.b) targetAlliance = Alliance.RED;
            if (gamepad1.x) targetAlliance = Alliance.BLUE;
            if (gamepad1.a) targetAlliance = Alliance.ALL;

            updateVisionTelemetry("INIT");
        }

        // Live updates during Auto
        while (opModeIsActive()) {
            updateVisionTelemetry("RUNNING");
        }

        limelight.stop();
    }

    //Pipeline name also for telementry and to make sure we are targeting correct Pipeline
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

    public HiveVisionData getHiveData() {
        HiveVisionData data = new HiveVisionData();
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
            for (LLResultTypes.FiducialResult tag : fiducials) {
                int id = tag.getFiducialId();
                if (!matchesAlliance(id)) continue;

                data.detected = true;
                data.tagId = id;
                data.hiveColor = getHiveColor(id);

                // Check roll to see if the tag is upside-down/tipped (>= 90 degrees)
                double rollDeg = tag.getTargetPoseCameraSpace().getOrientation().getRoll();
                data.isTipped = Math.abs(rollDeg) >= TIPPED_ROLL_THRESHOLD_DEG;
                data.isUpright = !data.isTipped;

                // Grab X, Y, Z, and Yaw relative to the tag
                Pose3D robotPose = tag.getRobotPoseTargetSpace();
                data.xInches    = robotPose.getPosition().x * METERS_TO_INCHES;
                data.yInches    = robotPose.getPosition().y * METERS_TO_INCHES;
                data.zInches    = robotPose.getPosition().z * METERS_TO_INCHES;
                data.yawDegrees = robotPose.getOrientation().getYaw();
                break;
            }
        }
        return data;
    }

    private boolean matchesAlliance(int id) {
        switch (targetAlliance) {
            case RED:
                return id >= RED_TAG_MIN && id <= RED_TAG_MAX;
            case BLUE:
                return id >= BLUE_TAG_MIN && id <= BLUE_TAG_MAX;
            case ALL:
            default:
                return (id >= RED_TAG_MIN && id <= RED_TAG_MAX)
                        || (id >= BLUE_TAG_MIN && id <= BLUE_TAG_MAX);
        }
    }

    private String getHiveColor(int id) {
        if (id >= RED_TAG_MIN && id <= RED_TAG_MAX) {
            return "RED";
        } else if (id >= BLUE_TAG_MIN && id <= BLUE_TAG_MAX) {
            return "BLUE";
        }
        return "UNKNOWN";
    }

    private void updateVisionTelemetry(String status) {
        HiveVisionData hive = getHiveData();
        telemetry.addData("Mode Status", status);
        telemetry.addData("Active Pipeline", getPipelineName(PIPELINE_ALL_TAGS));
        telemetry.addData("Target Alliance", targetAlliance + " (B=Red | X=Blue | A=All)");
        telemetry.addLine("--------------------------------");

        if (hive.detected) {
            telemetry.addData("Tag Detected ID", hive.tagId);
            telemetry.addData("Hive Color", hive.hiveColor);
            telemetry.addData("Hive Tipped", hive.isTipped);
            telemetry.addData("Hive Upright", hive.isUpright);
            telemetry.addData("Hive Status", hive.isTipped ? "TIPPED (Upside-Down)" : "UPRIGHT");
            telemetry.addData("Strafe (Left/Right) X (in)", String.format("%.1f", hive.xInches));
            telemetry.addData("Vertical (Up/Down) Y (in)", String.format("%.1f", hive.yInches));
            telemetry.addData("Distance Z (in)", String.format("%.1f", hive.zInches));
            telemetry.addData("Yaw Angle (deg)", String.format("%.1f", hive.yawDegrees));
        } else {
            telemetry.addLine("No matching AprilTag detected");
        }
        telemetry.update();
    }
}