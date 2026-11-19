package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.Paths;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import static com.pedropathing.api.Paths.*;


@Autonomous
public class Auto extends OpMode {
    private Follower follower;
    private Timer pathTimer;
    private Timer opModeTimer;

    public enum PathState {
        SHOOT,
        MOVETOINTAKE,
        LAUNCH,
        DONE
    }

    PathState pathState;

    private final Pose start = new Pose(56, 8, Math.toRadians(90));
    private final Pose intake = new Pose(18.0317, 47.225, Math.toRadians(179.5528));
    private final Pose control1 = new Pose(55.7261, 17.199, Math.toRadians(0));
    private final Pose control2 = new Pose(34.7374, 47.0946, Math.toRadians(0));

    private Path pathChain = Paths.path(pathToIntake(), pathToShoot());

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        pathState = PathState.SHOOT;
        pathTimer = new Timer();
        opModeTimer = new Timer();
    }

    @Override
    public void start() {
        opModeTimer.reset();
        setPathState(PathState.SHOOT);
    }

    // TODO: Add the launcher and intake actions. For now this only drives the paths.
    @Override
    public void loop() {
        follower.update();

        switch (pathState) {
            case SHOOT:
                // Shoot the preloaded pieces here, then drive to the intake.
                follower.follow(pathToIntake());
                setPathState(PathState.MOVETOINTAKE);
                break;
            case MOVETOINTAKE:
                if (!follower.isBusy()) {
                    follower.follow(pathToShoot());
                    setPathState(PathState.LAUNCH);
                }
                break;
            case LAUNCH:
                if (!follower.isBusy()) {
                    // Shoot the collected pieces here.
                    setPathState(PathState.DONE);
                }
                break;
            case DONE:
                break;
        }

        telemetry.addData("state", pathState);
        telemetry.addData("pose", follower.pose());
    }

    @Override
    public void stop() {
        follower.stop();
    }

    private Path pathToIntake(){
        return curve(start, control1, control2, intake);
    }
    private Path pathToShoot(){
        return curve(intake, control2, control1, start);
    }
    private void setPathState(PathState state){
        pathState = state;
        pathTimer.reset();
    }
}
