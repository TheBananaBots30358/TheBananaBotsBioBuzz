package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// No @Autonomous annotation here—this is the shared parent class
public abstract class TwoStageBasketAuto extends OpMode {
    private Limelight3A limelight3A;
    private ElapsedTime stateTimer = new ElapsedTime();

    // 4 + 4 Tag Groups for Red and Blue
    private static final Set<Integer> RED_GROUP_1 = new HashSet<>(Arrays.asList(34, 35, 36, 37));
    private static final Set<Integer> RED_GROUP_2 = new HashSet<>(Arrays.asList(33, 32, 31, 30));

    private static final Set<Integer> BLUE_GROUP_1 = new HashSet<>(Arrays.asList(45, 44, 43, 42));

    private static final Set<Integer> BLUE_GROUP_2 = new HashSet<>(Arrays.asList(38, 39, 40, 41));

    protected final boolean isRedAlliance;
    private final Set<Integer> firstFourTags;
    private final Set<Integer> secondFourTags;

    private enum AutoState {
        TARGET_FIRST_GROUP,
        SCORE_FIRST_BASKET,
        DRIVE_TO_SECOND_ZONE,
        TARGET_SECOND_GROUP,
        SCORE_SECOND_BASKET,
        DONE
    }

    private AutoState currentState = AutoState.TARGET_FIRST_GROUP;

    // Constructor called by RedBasketAuto and BlueBasketAuto
    protected TwoStageBasketAuto(boolean isRedAlliance) {
        this.isRedAlliance = isRedAlliance;
        this.firstFourTags = isRedAlliance ? RED_GROUP_1 : BLUE_GROUP_1;
        this.secondFourTags = isRedAlliance ? RED_GROUP_2 : BLUE_GROUP_2;
    }

    @Override
    public void init() {
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(0);
        limelight3A.start();

        telemetry.addData("Alliance", isRedAlliance ? "RED" : "BLUE");
        telemetry.addData("Status", "Initialized and ready");
        telemetry.update();
    }

    @Override
    public void start() {
        limelight3A.start();
        stateTimer.reset();
    }

    @Override
    public void loop() {
        switch (currentState) {
            case TARGET_FIRST_GROUP:
                LLResultTypes.FiducialResult tag1 = findBestTagFromSet(firstFourTags);
                if (tag1 != null) {
                    double tx = tag1.getTargetXDegrees();
                    double area = tag1.getTargetArea();

                    // TODO: driveTowardsTag(tx, area);

                    if (Math.abs(tx) < 2.0 && area >= 4.5) {
                        stopDrivetrain();
                        transitionTo(AutoState.SCORE_FIRST_BASKET);
                    }
                }
                if (stateTimer.seconds() > 4.0) {
                    stopDrivetrain();
                    transitionTo(AutoState.DRIVE_TO_SECOND_ZONE);
                }
                break;

            case SCORE_FIRST_BASKET:
                if (stateTimer.seconds() > 1.0) {
                    transitionTo(AutoState.DRIVE_TO_SECOND_ZONE);
                }
                break;

            case DRIVE_TO_SECOND_ZONE:
                // Use isRedAlliance if turning/strafing direction mirrors between Red and Blue!
                // double turnDirection = isRedAlliance ? 1.0 : -1.0;
                if (stateTimer.seconds() > 1.5) {
                    stopDrivetrain();
                    transitionTo(AutoState.TARGET_SECOND_GROUP);
                }
                break;

            case TARGET_SECOND_GROUP:
                LLResultTypes.FiducialResult tag2 = findBestTagFromSet(secondFourTags);
                if (tag2 != null) {
                    double tx = tag2.getTargetXDegrees();
                    double area = tag2.getTargetArea();

                    // TODO: driveTowardsTag(tx, area);

                    if (Math.abs(tx) < 2.0 && area >= 4.5) {
                        stopDrivetrain();
                        transitionTo(AutoState.SCORE_SECOND_BASKET);
                    }
                }
                if (stateTimer.seconds() > 4.0) {
                    stopDrivetrain();
                    transitionTo(AutoState.DONE);
                }
                break;

            case SCORE_SECOND_BASKET:
                if (stateTimer.seconds() > 1.0) {
                    transitionTo(AutoState.DONE);
                }
                break;

            case DONE:
                stopDrivetrain();
                break;
        }

        telemetry.addData("Alliance", isRedAlliance ? "RED" : "BLUE");
        telemetry.addData("State", currentState);
        telemetry.update();
    }

    private LLResultTypes.FiducialResult findBestTagFromSet(Set<Integer> allowedIds) {
        LLResult llResult = limelight3A.getLatestResult();
        if (llResult == null || !llResult.isValid()) return null;

        LLResultTypes.FiducialResult bestMatch = null;
        double maxArea = -1.0;

        for (LLResultTypes.FiducialResult tag : llResult.getFiducialResults()) {
            if (allowedIds.contains(tag.getFiducialId()) && tag.getTargetArea() > maxArea) {
                maxArea = tag.getTargetArea();
                bestMatch = tag;
            }
        }
        return bestMatch;
    }

    private void transitionTo(AutoState nextState) {
        currentState = nextState;
        stateTimer.reset();
    }

    private void stopDrivetrain() {
        // Set all drive motor powers to 0
    }
}