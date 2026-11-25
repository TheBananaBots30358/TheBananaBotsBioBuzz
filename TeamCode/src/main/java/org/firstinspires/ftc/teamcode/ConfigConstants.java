package org.firstinspires.ftc.teamcode;

public final class ConfigConstants {
    public static class NAMES {

        // Motors
        public static final String FRONT_LEFT_DRIVE_MOTOR = "frontLeft";
        public static final String FRONT_RIGHT_DRIVE_MOTOR = "frontRight";
        public static final String BACK_LEFT_DRIVE_MOTOR = "backLeft";
        public static final String BACK_RIGHT_DRIVE_MOTOR = "backRight";
        public static final String INTAKE_MOTOR = "intake";
        public static final String POLLEN_LAUNCHER = "pollenLauncher";
        public static final String NECTAR_LAUNCHER = "nectarLauncher";

        // Servos
        public static final String POLLEN_GATE = "pollenGate";
        public static final String NECTAR_GATE = "nectarGate";
        public static final String FLOWER_INTAKE = "FloInt";

        // Odometry
        public static final String ODOMETRY_COMPUTER = "pinpoint";

    }

    public static class VALUES {
        public static final double INTAKE_POWER = 0.725;
        public static final double FLOWER_UP_POSITION = 0.39;
        public static final double FLOWER_DOWN_POSITION = 0.13;
        public static final double NECTAR_VELOCITY = 1760;
        public static final double POLLEN_VELOCITY = -1660;
    }
}
