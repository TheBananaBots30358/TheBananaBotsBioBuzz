package org.firstinspires.ftc.teamcode;
import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class blueDownSide extends OpMode {

    public enum PathState {
        SPIN_UP,
        SHOOT,
        MOVE_TO_INTAKE,
        RETURN_TO_LAUNCH,
        LAUNCH,
        DONE
    }
    private PathState pathState;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(81.7258, 9.0468, 270);
    private final Pose path1 = poseFactory.of(94.75, 13.0115, 270);
    private final Pose path1Control1 = poseFactory.of(88.5223, 27.5, 0);

    private final Pose point281725890468Start = poseFactory.of(94.75, 13.0115, 270);
    private final Pose point281725890468 = poseFactory.of(81.7258, 9.0468, 270);
    private final Pose point281725890468Control1 = poseFactory.of(88.5, 27.5, 0);

    private final Robot robot = new Robot();
    private Follower follower;

    private int waitLoops = 0;

    @Override
    public void init() {
        robot.init(hardwareMap);
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
    }
    @Override
    public void start() {
        if (follower == null) {
            return;}
        waitLoops = 0;
        pathState = PathState.SPIN_UP;
    }
    @Override
    public void loop() {
        follower.update();
        switch (pathState) {
            case SPIN_UP:
                robot.pollenLauncher.setVelocity(-1860);
                robot.nectarLauncher.setVelocity(2160);
                if (robot.pollenLauncher.isReady()) {
                    robot.pollenLauncher.openGate();
                    robot.nectarLauncher.openGate();
                    waitLoops = 0;
                    pathState = PathState.SHOOT;
                }
                break;

            case SHOOT:
                robot.pollenLauncher.setVelocity(-1860);
                robot.nectarLauncher.setVelocity(2160);
                waitLoops++;
                if (waitLoops > 40) {
                    robot.pollenLauncher.stop();
                    robot.nectarLauncher.stop();
                    robot.intake.toggleFlower();
                    robot.intake.start();
                    follower.follow(pathToFlower());
                    waitLoops = 0;
                    pathState = PathState.MOVE_TO_INTAKE;
                }
                break;
            case MOVE_TO_INTAKE:
                if (!follower.isBusy()) {
                    waitLoops++;
                    if (waitLoops > 25) {
                        robot.intake.stop();
                        robot.intake.toggleFlower();
                        follower.follow(pathToLaunch());
                        waitLoops = 0;
                        pathState = PathState.RETURN_TO_LAUNCH;
                    }
                }
                break;
            case RETURN_TO_LAUNCH:
                robot.pollenLauncher.setVelocity(-1860);
                robot.nectarLauncher.setVelocity(2160);
                if (!follower.isBusy()) {
                    waitLoops = 0;
                    pathState = PathState.LAUNCH;
                }
                break;

            case LAUNCH:
                robot.pollenLauncher.setVelocity(-1860);
                robot.nectarLauncher.setVelocity(2160);
                if (robot.pollenLauncher.isReady()) {
                    robot.pollenLauncher.openGate();
                    robot.nectarLauncher.openGate();
                    waitLoops++;
                }
                if (waitLoops > 40) {
                    robot.pollenLauncher.stop();
                    robot.nectarLauncher.stop();
                    pathState = PathState.DONE;
                }
                break;
            case DONE:
                robot.stopAll();
                break;
        }
        telemetry.addData("state", pathState);
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
    public Path pathToFlower() {
        return Paths.curve(start, path1Control1, path1).linear(start, path1);
    }
    public Path pathToLaunch() {
        return Paths.curve(point281725890468Start, point281725890468Control1, point281725890468)
                .linear(point281725890468Start, point281725890468);
    }
}
