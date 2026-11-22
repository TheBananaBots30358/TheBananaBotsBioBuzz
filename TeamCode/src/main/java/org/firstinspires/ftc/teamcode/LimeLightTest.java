package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@Autonomous(name = "LimeLight Test", group = "Test")
public class LimeLightTest extends LinearOpMode {

    private Limelight3A limelight;

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

        // Live preview during Init
        while (!isStarted() && !isStopRequested()) {
            updateVisionTelemetry("INIT");
        }

        // Live updates during Auto
        while (opModeIsActive()) {
            updateVisionTelemetry("RUNNING");
        }

        limelight.stop();
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

    public HiveVisionData getHiveData() {
        HiveVisionData data = new HiveVisionData();
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
            if (!fiducials.isEmpty()) {
                LLResultTypes.FiducialResult tag = fiducials.get(0);
                data.detected = true;
                data.tagId = tag.getFiducialId();
                data.hiveColor = getHiveColor(data.tagId);

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
            }
        }
        return data;
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
            telemetry.addLine("No AprilTag detected");
        }
        telemetry.update();
    }
}