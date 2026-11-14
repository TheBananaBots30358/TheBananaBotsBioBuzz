package org.firstinspires.ftc.teamcode;
import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.pedro.Constants;
@Autonomous
public class UpSide extends OpMode {
    public enum PathState {
        PRELOAD_SPIN_UP,
        PRELOAD_SHOOT,
        DRIVE_TO_FLOWER_1,
        FLOWER_NUDGE_FWD_1,
        FLOWER_NUDGE_BACK_1,
        DRIVE_TO_GOAL_1,
        CYCLE_1_SPIN_UP,
        CYCLE_1_SHOOT,
        DRIVE_TO_FLOWER_2,
        FLOWER_NUDGE_FWD_2,
        FLOWER_NUDGE_BACK_2,
        DRIVE_TO_GOAL_2,
        CYCLE_2_SPIN_UP,
        CYCLE_2_SHOOT,
        PARK,
        DONE
    }
    private PathState pathState = PathState.DONE;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose startPose = poseFactory.of(52, 9.3364, 270);
    private final Pose flowerControlPose = poseFactory.of(20, 46.75, 180);
    private final Pose flowerIntakePose = poseFactory.of(17.5, 46.75, 180);
    private final Pose flowerPushPose = poseFactory.of(12.5, 43.75, 180);
    private final Pose goalControl1 = poseFactory.of(19.9383, 90.0549, 0);
    private final Pose goalControl2 = poseFactory.of(56.4254, 118.7839, 0);
    private final Pose goalScorePose = poseFactory.of(64, 130, 90);
    private final Pose flowerIntakePose2 = poseFactory.of(48.0691, 118.3273, 90);
    private final Pose flowerNudgePose2 = poseFactory.of(48.0691, 120.9554, 90);
    private final Pose parkPose = poseFactory.of(8.1779, 116.872, 0);
    private final Pose parkControlPose = poseFactory.of(49.2363, 111.4036, 0);

    private Path startToFlowerPath;
    private Path flowerNudgeFwdPath;
    private Path flowerNudgeBackPath;
    private Path flowerToGoalPath;
    private Path goalToFlowerPath2;
    private Path flowerPushPath2;
    private Path flowerBackPath2;
    private Path flowerToGoalPath2;
    private Path parkPath;
    private final Robot robot = new Robot();
    private Follower follower;
    private final ElapsedTime stateTimer = new ElapsedTime();
    private static final double POLLEN_TARGET_VELO = -1860;
    private static final double NECTAR_TARGET_VELO = 2160;
    private static final double SHOOT_DURATION_SEC = 4.75;
    private static final double PATH_DONE_T = 0.96;
    private boolean actionTriggered = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        startToFlowerPath = Paths.curve(startPose, flowerControlPose, flowerIntakePose)
                .linear(startPose, flowerIntakePose);
        flowerNudgeFwdPath = Paths.line(flowerIntakePose, flowerPushPose)
                .linear(flowerIntakePose, flowerPushPose);
        flowerNudgeBackPath = Paths.line(flowerPushPose, flowerIntakePose)
                .linear(flowerPushPose, flowerIntakePose);
        flowerToGoalPath = Paths.curve(flowerIntakePose, goalControl1, goalControl2, goalScorePose)
                .linear(flowerIntakePose, goalScorePose);
        goalToFlowerPath2 = Paths.line(goalScorePose, flowerIntakePose2)
                .linear(goalScorePose, flowerIntakePose2);
        flowerPushPath2 = Paths.line(flowerIntakePose2, flowerNudgePose2)
                .linear(flowerIntakePose2, flowerNudgePose2);
        flowerBackPath2 = Paths.line(flowerNudgePose2, flowerIntakePose2)
                .linear(goalScorePose, flowerIntakePose2);
        flowerToGoalPath2 = Paths.line(flowerIntakePose2, goalScorePose)
                .linear(flowerIntakePose2, goalScorePose);
        parkPath = Paths.line(goalScorePose, parkPose)
                .linear(goalScorePose, parkPose);
    }

    @Override
    public void start() {
        if (follower == null) {
            pathState = PathState.DONE;
            return;
        }
        closeGates();
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
                if (isAtShootingSpeed()) {
                    openGates();
                    stateTimer.reset();
                    pathState = PathState.PRELOAD_SHOOT;
                }
                break;
            case PRELOAD_SHOOT:
                spinUpLaunchers();
                if (stateTimer.seconds() >= SHOOT_DURATION_SEC) {
                    closeGates();
                    stopLaunchers();
                    follower.follow(startToFlowerPath);
                    actionTriggered = false;
                    stateTimer.reset();
                    pathState = PathState.DRIVE_TO_FLOWER_1;
                }
                break;
            case DRIVE_TO_FLOWER_1:
                if (!actionTriggered && follower.completion() >= 0.25) {
                    robot.intake.toggleFlower();
                    robot.intake.start();
                    actionTriggered = true;
                }
                if (!follower.following() || follower.completion() >= PATH_DONE_T) {
                    follower.follow(flowerNudgeFwdPath);
                    stateTimer.reset();
                    pathState = PathState.FLOWER_NUDGE_FWD_1;
                }
                break;
            case FLOWER_NUDGE_FWD_1:
                if ((!follower.following() || follower.completion() >= PATH_DONE_T) && stateTimer.milliseconds() > 2000) {
                    follower.follow(flowerNudgeBackPath);
                    stateTimer.reset();
                    pathState = PathState.FLOWER_NUDGE_BACK_1;
                }
                break;
            case FLOWER_NUDGE_BACK_1:
                if (!follower.following() || follower.completion() >= PATH_DONE_T) {
                    robot.intake.stop();
                    robot.intake.toggleFlower();
                    follower.follow(flowerToGoalPath);
                    actionTriggered = false;
                    stateTimer.reset();
                    pathState = PathState.DRIVE_TO_GOAL_1;
                }
                break;
            case DRIVE_TO_GOAL_1:
                if (follower.completion() >= 0.40) {
                    spinUpLaunchers();
                }
                if (!follower.following() || follower.completion() >= PATH_DONE_T) {
                    stateTimer.reset();
                    pathState = PathState.CYCLE_1_SPIN_UP;
                }
                break;
            case CYCLE_1_SPIN_UP:
                spinUpLaunchers();
                if (isAtShootingSpeed()) {
                    openGates();
                    stateTimer.reset();
                    pathState = PathState.CYCLE_1_SHOOT;
                }
                break;
            case CYCLE_1_SHOOT:
                spinUpLaunchers();
                if (stateTimer.seconds() >= SHOOT_DURATION_SEC) {
                    closeGates();
                    stopLaunchers();
                    follower.follow(goalToFlowerPath2);
                    actionTriggered = false;
                    stateTimer.reset();
                    pathState = PathState.DRIVE_TO_FLOWER_2;
                }
                break;
            case DRIVE_TO_FLOWER_2:
                if (!actionTriggered && follower.completion() >= 0.50) {
                    robot.intake.toggleFlower();
                    robot.intake.start();
                    actionTriggered = true;
                }
                if (!follower.following() || follower.completion() >= PATH_DONE_T) {
                    follower.follow(flowerPushPath2);
                    stateTimer.reset();
                    pathState = PathState.FLOWER_NUDGE_FWD_2;
                }
                break;
            case FLOWER_NUDGE_FWD_2:
                if (!follower.following() || follower.completion() >= PATH_DONE_T) {
                    follower.follow(flowerBackPath2);
                    stateTimer.reset();
                    pathState = PathState.FLOWER_NUDGE_BACK_2;
                }
                break;
            case FLOWER_NUDGE_BACK_2:
                if (!follower.following() || follower.completion() >= PATH_DONE_T) {
                    robot.intake.stop();
                    robot.intake.toggleFlower();
                    follower.follow(flowerToGoalPath2);
                    actionTriggered = false;
                    stateTimer.reset();
                    pathState = PathState.DRIVE_TO_GOAL_2;
                }
                break;
            case DRIVE_TO_GOAL_2:
                if (follower.completion() >= 0.40) {
                    spinUpLaunchers();
                }
                if (!follower.following() || follower.completion() >= PATH_DONE_T) {
                    stateTimer.reset();
                    pathState = PathState.CYCLE_2_SPIN_UP;
                }
                break;
            case CYCLE_2_SPIN_UP:
                spinUpLaunchers();
                if (isAtShootingSpeed() || stateTimer.seconds() > 2.0) {
                    openGates();
                    stateTimer.reset();
                    pathState = PathState.CYCLE_2_SHOOT;
                }
                break;
            case CYCLE_2_SHOOT:
                spinUpLaunchers();
                if (stateTimer.seconds() >= SHOOT_DURATION_SEC) {
                    follower.follow(parkPath);
                    stopLaunchers();
                    pathState = PathState.PARK;
                }
                break;
            case PARK:
                if (follower.completion() >= PATH_DONE_T) {
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
    private boolean isAtShootingSpeed() {
        return robot.pollenLauncher.isReady() && robot.nectarLauncher.isReady();
    }
    private void spinUpLaunchers() {
        robot.pollenLauncher.setVelocity(POLLEN_TARGET_VELO);
        //robot.nectarLauncher.setVelocity(NECTAR_TARGET_VELO);
    }
    private void openGates() {
        robot.pollenLauncher.openGate();
        //robot.nectarLauncher.openGate();
    }
    private void closeGates() {
        robot.pollenLauncher.closeGate();
        //robot.nectarLauncher.closeGate();
    }
    private void stopLaunchers() {
        robot.pollenLauncher.stop();
        //robot.nectarLauncher.stop();
    }
}