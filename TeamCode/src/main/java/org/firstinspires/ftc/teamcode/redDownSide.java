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
public class redDownSide extends OpMode {
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

    private final Pose start = new Pose(59.59, 132.01, Math.toRadians(90));
    private final Pose intake = new Pose(46.73, 112.69, Math.toRadians(179.5528));
    private final Pose control1 = new Pose(61.88, 108.92, Math.toRadians(0));
    private final Pose intake1 = new Pose(46.73, 125.43, Math.toRadians(179.5528));
    private final Pose control2 = new Pose(56.68, 122.45, Math.toRadians(0));


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
        return curve(start, control1, intake);
    }
    private Path pathToShoot(){
        return curve(intake1, control2, start);
    }
    private void setPathState(PathState state){
        pathState = state;
        pathTimer.reset();
    }
}

//[(59.725, 132.403), (61.924, 108.947), (46.517, 112.688), (46.729, 125.428), (56.614, 122.524), (59.615, 132.006)]