package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

public class LimelightSystem {

    public enum Alliance {
        RED,
        BLUE,
        ALL // Useful for desk testing when you want to see every tag
    }

    private final Limelight3A limelight;
    private Alliance targetAlliance;

    private static final double METERS_TO_INCHES = 39.3701;
    private static final int PIPELINE_ALL_TAGS = 0;

    // Public data container returned to Autonomous OpModes
    public static class HiveVisionData {
        public boolean detected = false;
        public int tagId = -1;
        public String hiveColor = "NONE";
        public boolean isTipped = false;
        boolean isUpright = false;
        public double xInches = 0.0;    // Strafe (Left/Right)
        public double yInches = 0.0;    // Vertical (Up/Down)
        public double zInches = 0.0;    // Distance (Forward/Back)
        public double yawDegrees = 0.0; // Heading angle relative to the tag
    }

    // Constructor requiring an Alliance (RED, BLUE, or ALL)
    public LimelightSystem(HardwareMap hardwareMap, Alliance alliance) {
        this.targetAlliance = alliance;
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(PIPELINE_ALL_TAGS);
        limelight.setPollRateHz(100);
    }

    // Optional setter if your Auto team lets drivers toggle alliance on a gamepad during Init
    public void setAlliance(Alliance alliance) {
        this.targetAlliance = alliance;
    }

    public void start() {
        limelight.start();
    }

    public void stop() {
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

            // Loop through all detected tags and grab the first one matching our alliance
            for (LLResultTypes.FiducialResult tag : fiducials) {
                int id = tag.getFiducialId();
                if (!matchesAlliance(id)) {
                    continue; // Ignore opponent or unknown tags
                }

                data.detected = true;
                data.tagId = id;
                data.hiveColor = getHiveColor(id);

                // Check roll to see if the tag is upside-down/tipped (>= 90 degrees)
                double rollDeg = tag.getTargetPoseCameraSpace().getOrientation().getRoll();
                data.isTipped = Math.abs(rollDeg) >= 90.0;
                data.isUpright = Math.abs(rollDeg) < 90.0;


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
                return id >= 30 && id <= 37;
            case BLUE:
                return id >= 38 && id <= 45;
            case ALL:
            default:
                return (id >= 30 && id <= 45);
        }
    }

    private String getHiveColor(int id) {
        if (id >= 30 && id <= 37) {
            return "RED";
        } else if (id >= 38 && id <= 45) {
            return "BLUE";
        }
        return "UNKNOWN";
    }

    public void addTelemetry(Telemetry telemetry, HiveVisionData hive) {
        telemetry.addData("Target Alliance", targetAlliance);
        telemetry.addData("Active Pipeline", getPipelineName(PIPELINE_ALL_TAGS));
        telemetry.addLine("--------------------------------");

        if (hive.detected) {
            telemetry.addData("Tag Detected ID", hive.tagId);
            telemetry.addData("Hive Color", hive.hiveColor);
            telemetry.addData("Hive Tipped", hive.isTipped);
            telemetry.addData("Hive Status", hive.isUpright ? "UPRIGHT" : "TIPPED (Upside-Down)");
            telemetry.addData("Strafe (Left/Right) X (in)", String.format("%.1f", hive.xInches));
            telemetry.addData("Vertical (Up/Down) Y (in)", String.format("%.1f", hive.yInches));
            telemetry.addData("Distance Z (in)", String.format("%.1f", hive.zInches));
            telemetry.addData("Yaw Angle (deg)", String.format("%.1f", hive.yawDegrees));

            /*telemetry.addData("Pos (X, Y, Z) in", String.format("%.1f, %.1f, %.1f",
                    hive.xInches, hive.yInches, hive.zInches));*/
        } else {
            telemetry.addLine("No matching AprilTag detected");
        }
    }
}