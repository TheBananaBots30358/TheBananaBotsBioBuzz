package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * Autonomous period, written as a state machine.
 *
 * Each State is one step. loop() runs over and over; each time it checks the current
 * step and decides whether to move on to the next one.
 *
 * Pedro Pathing drives the robot. The launchers and intake come from Robot.java.
 */
@Autonomous(name = "Auto Main", group = "Competition")
public class AutoMain extends OpMode {

    private enum State {
        DRIVE_TO_LAUNCH,
        SPIN_UP,
        SHOOT,
        DRIVE_TO_PARK,
        DONE
    }

    // TODO: Measure these on the field. Units are inches; heading is in radians.
    private final Pose startPose = new Pose(0, 0, 0);
    private final Pose launchPose = new Pose(24, 0, 0);
    private final Pose parkPose = new Pose(24, 24, 0);

    private static final double SHOOT_TIME_SECONDS = 1.0;

    private final Robot robot = new Robot();
    private final ElapsedTime stateTimer = new ElapsedTime();
    private Follower follower;
    private State state;

    private Path toLaunch;
    private Path toPark;

    @Override
    public void init() {
        robot.init(hardwareMap);

        // Create the follower after robot.init() so Pedro's motor setup is the one used.
        follower = Constants.create(hardwareMap);
        if (follower == null) {
            telemetry.addLine("Pedro Pathing is not set up yet. Run the tuners and fill in pedro/Constants.java.");
            return;
        }
        follower.setPose(startPose);

        toLaunch = line(startPose, launchPose).constant(launchPose);
        toPark = line(launchPose, parkPose).constant(parkPose);
    }

    @Override
    public void start() {
        if (follower == null) {
            return;
        }
        follower.follow(toLaunch);
        setState(State.DRIVE_TO_LAUNCH);
    }

    @Override
    public void loop() {
        if (follower == null) {
            telemetry.addLine("Pedro Pathing is not set up yet.");
            return;
        }
        follower.update();

        switch (state) {
            case DRIVE_TO_LAUNCH:
                robot.pollenLauncher.spinUp();
                if (!follower.isBusy()) {
                    setState(State.SPIN_UP);
                }
                break;

            case SPIN_UP:
                if (robot.pollenLauncher.isReady()) {
                    robot.pollenLauncher.shoot();
                    setState(State.SHOOT);
                }
                break;

            case SHOOT:
                if (stateTimer.seconds() > SHOOT_TIME_SECONDS) {
                    robot.pollenLauncher.stop();
                    follower.follow(toPark);
                    setState(State.DRIVE_TO_PARK);
                }
                break;

            case DRIVE_TO_PARK:
                if (!follower.isBusy()) {
                    setState(State.DONE);
                }
                break;

            case DONE:
                break;
        }

        telemetry.addData("State", state);
        telemetry.addData("Pose", follower.pose());
        robot.addTelemetry(telemetry);
    }

    @Override
    public void stop() {
        if (follower != null) {
            follower.stop();
        }
        robot.stopAll();
    }

    private void setState(State newState) {
        state = newState;
        stateTimer.reset();
    }
}
