package org.firstinspires.ftc.teamcode.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@Autonomous(name = "Auto - Limelight Hive (4 Pipelines)", group = "Autonomous")
public class AutoLimelightHive extends LinearOpMode {

    private Limelight3A limelight;
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private static final double METERS_TO_INCHES = 39.3701;

    // Your configured Limelight pipelines
    private static final int PIPELINE_RED_0  = 0; // IDs: 33, 32, 31, 30
    private static final int PIPELINE_RED_1  = 1; // IDs: 34, 35, 36, 37
    private static final int PIPELINE_BLUE_2 = 2; // IDs: 45, 44, 43, 42
    private static final int PIPELINE_BLUE_3 = 3; // IDs: 38, 39, 40, 41

    // CHANGE THIS FOR EACH AUTO OPMODE (or select with gamepad during Init below!)
    private int activePipeline = PIPELINE_RED_0;

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
        // 1. Initialize Drivetrain Motors
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // 2. Initialize Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(activePipeline);
        limelight.setPollRateHz(100);
        limelight.start();

        // 3. Init Loop: Lets you switch pipelines with Gamepad 1 buttons before pressing Play!
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

            HiveVisionData preview = getHiveData();
            telemetry.addLine("=== PRESS GAMEPAD 1 TO PICK PIPELINE ===");
            telemetry.addLine("B = Red 0 (30-33) | A = Red 1 (34-37)");
            telemetry.addLine("X = Blue 2 (42-45) | Y = Blue 3 (38-41)");
            telemetry.addData("Active Pipeline", getPipelineName(activePipeline));
            telemetry.addData("Tag Seen", preview.detected ? preview.tagId : "NONE");
            telemetry.addData("Hive Status", preview.detected ? (preview.isUpright ? "UPRIGHT" : "TIPPED") : "N/A");
            telemetry.addData("Dist / Strafe (in)", String.format("%.1f / %.1f", preview.distanceInches, preview.strafeInches));
            telemetry.update();
        }

        waitForStart();

        if (opModeIsActive()) {
            HiveVisionData hive = getHiveData();

            if (hive.detected && hive.isUpright) {
                telemetry.addLine("Target Hive UPRIGHT -> Aligning...");
                telemetry.update();

                alignToHive(18.0, 3.5);
                // scoreInHive();

            } else if (hive.detected && !hive.isUpright) {
                telemetry.addLine("Target Hive TIPPED -> Switching to backup Hive...");
                telemetry.update();

                // Bonus: Since you have separate pipelines for each Hive cell,
                // you can switch to your alliance's other Hive pipeline right here!
                switchToPartnerHivePipeline();
                sleep(50); // Brief pause for pipeline switch
                // driveToOtherHiveAndScore();

            } else {
                telemetry.addLine("No tag seen in Pipeline " + activePipeline + " -> Running fallback...");
                telemetry.update();
            }
        }

        stopDrive();
        limelight.stop();
    }

    /**
     * Automatically switches to the other Hive pipeline on the same alliance if the first Hive is tipped.
     */
    private void switchToPartnerHivePipeline() {
        if (activePipeline == PIPELINE_RED_0) {
            activePipeline = PIPELINE_RED_1;
        } else if (activePipeline == PIPELINE_RED_1) {
            activePipeline = PIPELINE_RED_0;
        } else if (activePipeline == PIPELINE_BLUE_2) {
            activePipeline = PIPELINE_BLUE_3;
        } else if (activePipeline == PIPELINE_BLUE_3) {
            activePipeline = PIPELINE_BLUE_2;
        }
        limelight.pipelineSwitch(activePipeline);
    }

    private String getPipelineName(int index) {
        switch (index) {
            case 0: return "0: Red0 (IDs 30-33)";
            case 1: return "1: Red1 (IDs 34-37)";
            case 2: return "2: Blue2 (IDs 42-45)";
            case 3: return "3: Blue3 (IDs 38-41)";
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

                // Check if Hive cell is upright or tipped upside-down
                double tagRollDeg = tag.getTargetPoseCameraSpace().getOrientation().getRoll();
                data.isUpright = Math.abs(tagRollDeg) < 90.0;

                // Robot position relative to the Hive
                Pose3D robotPose = tag.getRobotPoseTargetSpace();
                data.strafeInches   = robotPose.getPosition().x * METERS_TO_INCHES;
                data.distanceInches = robotPose.getPosition().z * METERS_TO_INCHES;
                data.yawDegrees     = robotPose.getOrientation().getYaw();
            }
        }
        return data;
    }

    private void alignToHive(double targetDistanceInches, double timeoutSeconds) {
        ElapsedTime timer = new ElapsedTime();

        double kP_Forward = 0.035;
        double kP_Strafe  = 0.045;
        double kP_Turn    = 0.020;

        while (opModeIsActive() && timer.seconds() < timeoutSeconds) {
            HiveVisionData hive = getHiveData();

            if (!hive.detected) {
                stopDrive();
                continue;
            }

            double distanceError = Math.abs(hive.distanceInches) - targetDistanceInches;
            double strafeError   = hive.strafeInches;
            double turnError     = hive.yawDegrees;

            if (Math.abs(distanceError) < 1.5 && Math.abs(strafeError) < 1.5 && Math.abs(turnError) < 2.0) {
                break;
            }

            double forward = Range.clip(distanceError * kP_Forward, -0.5, 0.5);
            double strafe  = Range.clip(-strafeError * kP_Strafe, -0.5, 0.5);
            double turn    = Range.clip(-turnError * kP_Turn, -0.4, 0.4);

            driveMecanum(forward, strafe, turn);

            telemetry.addData("Pipeline", getPipelineName(activePipeline));
            telemetry.addData("Dist Err (in)", String.format("%.1f", distanceError));
            telemetry.addData("Strafe Err (in)", String.format("%.1f", strafeError));
            telemetry.addData("Yaw Err (deg)", String.format("%.1f", turnError));
            telemetry.update();
        }

        stopDrive();
    }

    private void driveMecanum(double forward, double strafe, double turn) {
        double fl = forward + strafe + turn;
        double fr = forward - strafe - turn;
        double bl = forward - strafe + turn;
        double br = forward + strafe - turn;

        double max = Math.max(Math.abs(fl), Math.max(Math.abs(fr), Math.max(Math.abs(bl), Math.abs(br))));
        if (max > 1.0) {
            fl /= max;
            fr /= max;
            bl /= max;
            br /= max;
        }

        frontLeft.setPower(fl);
        frontRight.setPower(fr);
        backLeft.setPower(bl);
        backRight.setPower(br);
    }

    private void stopDrive() {
        driveMecanum(0, 0, 0);
    }

    private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        frontLeft.setZeroPowerBehavior(behavior);
        frontRight.setZeroPowerBehavior(behavior);
        backLeft.setZeroPowerBehavior(behavior);
        backRight.setZeroPowerBehavior(behavior);
    }
}