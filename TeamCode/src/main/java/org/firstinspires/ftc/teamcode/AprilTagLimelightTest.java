package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@Autonomous(name = "AprilTag Limelight Test", group = "Test")
public class AprilTagLimelightTest extends OpMode {
    private Limelight3A limelight3A;

    @Override
    public void init() {
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(0); //0 for All AprilTag ID
        limelight3A.start();
        telemetry.addData("Status", "Initialized. Waiting for start...");

    }

    @Override
    public void start() {
        limelight3A.start();


        telemetry.update();
    }

    @Override
    public void loop() {
        LLResult llResult = limelight3A.getLatestResult();
        if (llResult != null && llResult.isValid()) {
            /*telemetry.addData("Target X offset", llResult.getTx());
            telemetry.addData("Target Y offset", llResult.getTy());
            telemetry.addData("Target Area offset", llResult.getTa());*/

            Pose3D botpose = llResult.getBotpose();
            telemetry.addData("tx", llResult.getTx());
            telemetry.addData("ty", llResult.getTy());
            telemetry.addData("Botpose", botpose.toString());

            for (LLResultTypes.FiducialResult tag : llResult.getFiducialResults()) {
                telemetry.addData("Tag " + tag.getFiducialId(), "%.1f deg left/right",
                        tag.getTargetXDegrees());
            }

           // List<LLResultTypes.FiducialResult> fiducials = llResult.getFiducialResults();

            //if (!fiducials.isEmpty()) {
                // Grab the primary (largest/closest) AprilTag detected on the Hive cell
                /*LLResultTypes.FiducialResult tag = fiducials.get(0);
                int tagId = tag.getFiducialId();
                telemetry.addLine("=== HIVE STATUS ===");
                telemetry.addData("Detected Tag ID", tagId);
                }
                */


                /*
                import com.qualcomm.hardware.limelightvision.LLResultTypes;
static final int TAGS = 3;
limelight.pipelineSwitch(TAGS);
LLResult r = freshResult(limelight, TAGS);
if (r != null) {
 for (LLResultTypes.FiducialResult tag : r.getFiducialResults()) {
 telemetry.addData("Tag " + tag.getFiducialId(), "%.1f deg left/right",
 tag.getTargetXDegrees());
 }
}
                 */
            }
        }
    }

