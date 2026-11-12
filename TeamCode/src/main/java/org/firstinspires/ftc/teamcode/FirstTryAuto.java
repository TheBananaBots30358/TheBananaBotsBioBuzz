package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

public class FirstTryAuto extends OpMode {
    private Follower follower;
    private final Pose start = new Pose(56, 8, 90);
    private final Pose end = new Pose(55.6916, 24.8691, 180);
    private final Path testForward = buildPath();
    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void loop() {
        follower.update();
        follower.follow(testForward);
    }

    private Path buildPath(){
        return line(start, end).tangent();
    }
}
