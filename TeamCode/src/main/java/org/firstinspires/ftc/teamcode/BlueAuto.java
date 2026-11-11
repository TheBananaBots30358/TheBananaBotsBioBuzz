
package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.ConfigConstants;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import static com.pedropathing.api.Paths.line;

@Autonomous(name = "Blue Auto")
public class BlueAuto extends OpMode {
    Follower follower;
    DcMotorEx pollenLauncher;
    Servo pollenGate;
    ElapsedTime timer = new ElapsedTime();

    PoseFactory p = PoseFactory.degrees();
    Pose start = p.of(100, 12, 90);
    Pose shoot = p.of(100, 30, 90);

    int step = 0;
    final double TARGET_RPM = 1860.0;   // sets target of rpm to 1860

@Override
public void init() {

    follower = Constants.create(hardwareMap);
    follower.setPose(start);

    pollenLauncher = hardwareMap.get(
            DcMotorEx.class,
            ConfigConstants.NAMES.POLLEN_LAUNCHER     //connects constants file of pollen launcher to this
    );

    pollenGate = hardwareMap.get(
            Servo.class,
            ConfigConstants.NAMES.POLLEN_GATE    //connects constants file of pollen gate to this
    );

    pollenLauncher.setMode(
            DcMotor.RunMode.RUN_USING_ENCODER
    );

    pollenGate.setPosition();

}



}

