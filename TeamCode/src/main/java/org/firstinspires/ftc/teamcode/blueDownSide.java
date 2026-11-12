package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import static com.pedropathing.api.Paths.*;


@Autonomous
public class blueDownSide extends OpMode {
    private Follower follower;
    private Timer pathTimer;
    private Timer opModeTimer;

    public enum PathState {
        SHOOT,
        MOVETOINTAKE,
        LAUNCH
    }

    PathState pathState;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(81.7258, 9.0468, 270);
    private final Pose path1 = poseFactory.of(94.75, 13.0115, 270);
    private final Pose path1Control1 = poseFactory.of(88.5223, 27.5, 0);
    private final Pose point281725890468Start = poseFactory.of(94.75, 13.0115, 270);
    private final Pose point281725890468 = poseFactory.of(81.7258, 9.0468, 270);
    private final Pose point281725890468Control1 = poseFactory.of(88.5, 27.5, 0);
    private Path pathChain = Paths.path(path1(), path281725890468());

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        pathState = PathState.SHOOT;
        pathTimer = new Timer();
        opModeTimer = new Timer();
    }

    @Override
    public void loop() {

    }


    public Path path1() {
        return Paths.curve(start, path1Control1, path1).linear(start, path1);
    }

    public Path path281725890468() {
        return Paths.curve(point281725890468Start, point281725890468Control1, point281725890468).linear(point281725890468Start, point281725890468);
    }
}

//[(59.725, 132.403), (61.924, 108.947), (46.517, 112.688), (46.729, 125.428), (56.614, 122.524), (59.615, 132.006)]