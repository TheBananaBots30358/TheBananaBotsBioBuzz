package org.firstinspires.ftc.teamcode;
import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;
@Autonomous
public class redCarry extends OpMode {
    public enum PathState {
        PRELOAD_SPIN_UP,
        PRELOAD_SHOOT,
        DRIVE_TO_FLOWER,
        INTAKE_FROM_FLOWER_WAIT,
        DRIVE_TO_GOAL,
        CYCLE_SPIN_UP,
        CYCLE_SHOOT,
        DONE
    }
    private PathState pathState = PathState.DONE;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose startPose = poseFactory.of(52, 9.3364, 270);
    private final Pose flowerControlPose = poseFactory.of(20, 46.75, 180);
    private final Pose flowerIntakePose = poseFactory.of(17.5, 46.75, 180);
    private final Pose goalControl1 = poseFactory.of(46.75, 123, 90);
    private final Pose goalControl2 = poseFactory.of(46.7946, 125.9554, 90);
    private final Pose goalScorePose = poseFactory.of(64, 132, 90);
    private Path toFlowerPath;
    private Path toGoalPath;
    private final Robot robot = new Robot();
    private Follower follower;
    private final ElapsedTime stateTimer = new ElapsedTime();
    private static final double POLLEN_TARGET_VELO = -1860;
    private static final double NECTAR_TARGET_VELO = 2160;
    private static final double SHOOT_DURATION_SEC = 1.2;
    private static final double INTAKE_SETTLE_SEC = 0.4;
    private static final double PATH_DONE_T = 0.96;
    private boolean actionTriggered = false;
    @Override
    public void init() {
        robot.init(hardwareMap);
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        toFlowerPath = Paths.curve(startPose, flowerControlPose, flowerIntakePose)
                .linear(startPose, flowerIntakePose);
        toGoalPath = Paths.curve(flowerIntakePose, goalControl1, goalControl2, goalScorePose)
                .linear(flowerIntakePose, goalScorePose);
    }
    @Override
    public void start() {
        if (follower == null) {
            pathState = PathState.DONE;
            return;
        }
        stateTimer.reset();
        actionTriggered = false;
        pathState = PathState.PRELOAD_SPIN_UP;
    }
    @Override
    public void loop() {
        follower.update();
        switch (pathState) {
            case PRELOAD_SPIN_UP:
                spinUpLaunchers();
                if ((robot.pollenLauncher.isReady() && robot.nectarLauncher.isReady()) || stateTimer.seconds() > 2.0) {
                    openGates();
                    stateTimer.reset();
                    pathState = PathState.PRELOAD_SHOOT;
                }
                break;
            case PRELOAD_SHOOT:
                spinUpLaunchers();
                if (stateTimer.seconds() >= SHOOT_DURATION_SEC) {
                    stopLaunchers();
                    follower.follow(toFlowerPath);
                    actionTriggered = false;
                    stateTimer.reset();
                    pathState = PathState.DRIVE_TO_FLOWER;
                }
                break;

            case DRIVE_TO_FLOWER:
                if (!actionTriggered && follower.completion() >= 0.25) {
                    robot.intake.toggleFlower();
                    robot.intake.start();
                    actionTriggered = true;
                }
                if (!follower.following() || follower.completion() >= PATH_DONE_T) {
                    stateTimer.reset();
                    pathState = PathState.INTAKE_FROM_FLOWER_WAIT;
                }
                break;
            case INTAKE_FROM_FLOWER_WAIT:
                if (stateTimer.seconds() >= INTAKE_SETTLE_SEC) {
                    robot.intake.stop();
                    robot.intake.toggleFlower();
                    follower.follow(toGoalPath);
                    actionTriggered = false;
                    stateTimer.reset();
                    pathState = PathState.DRIVE_TO_GOAL;
                }
                break;
            case DRIVE_TO_GOAL:
                if (follower.completion() >= 0.40) {
                    spinUpLaunchers();
                }
                if (!follower.following() || follower.completion() >= PATH_DONE_T) {
                    stateTimer.reset();
                    pathState = PathState.CYCLE_SPIN_UP;
                }
                break;
            case CYCLE_SPIN_UP:
                spinUpLaunchers();
                if ((robot.pollenLauncher.isReady() && robot.nectarLauncher.isReady()) || stateTimer.seconds() > 1.5) {
                    openGates();
                    stateTimer.reset();
                    pathState = PathState.CYCLE_SHOOT;
                }
                break;
            case CYCLE_SHOOT:
                spinUpLaunchers();
                if (stateTimer.seconds() >= SHOOT_DURATION_SEC) {
                    stopLaunchers();
                    pathState = PathState.DONE;
                }
                break;
            case DONE:
                robot.stopAll();
                break;
        }
        telemetry.addData("state", pathState);
        telemetry.addData("state time", "%.2f", stateTimer.seconds());
        telemetry.addData("pose", follower.pose());
        robot.addTelemetry(telemetry);
    }
    @Override
    public void stop() {
        if (follower != null) {
            follower.stop();
        }
        robot.stopAll();
    }
    private void spinUpLaunchers() {
        robot.pollenLauncher.setVelocity(POLLEN_TARGET_VELO);
        robot.nectarLauncher.setVelocity(NECTAR_TARGET_VELO);
    }
    private void openGates() {
        robot.pollenLauncher.openGate();
        robot.nectarLauncher.openGate();
    }
    private void stopLaunchers() {
        robot.pollenLauncher.stop();
        robot.nectarLauncher.stop();
    }
}
