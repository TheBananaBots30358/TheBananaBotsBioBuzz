package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ConfigConstants;

/**
 * Mecanum drivetrain used in TeleOp.
 * OWNER: Drivetrain team.
 *
 * In Autonomous, Pedro Pathing drives the robot instead (see pedro/Constants.java).
 */
public class Drivetrain {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    public void init(HardwareMap hwMap) {
        frontLeft = hwMap.get(DcMotor.class, ConfigConstants.NAMES.FRONT_LEFT_DRIVE_MOTOR);
        frontRight = hwMap.get(DcMotor.class, ConfigConstants.NAMES.FRONT_RIGHT_DRIVE_MOTOR);
        backLeft = hwMap.get(DcMotor.class, ConfigConstants.NAMES.BACK_LEFT_DRIVE_MOTOR);
        backRight = hwMap.get(DcMotor.class, ConfigConstants.NAMES.BACK_RIGHT_DRIVE_MOTOR);

        // TODO: Check these on the real robot. Run "Test: Drivetrain" and push the stick
        // forward. Every wheel should spin forward. If one spins backward, flip its direction.
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Drive the robot relative to where it is facing.
     *
     * @param forward positive drives forward (-1 to 1)
     * @param right   positive strafes right (-1 to 1)
     * @param rotate  positive turns clockwise (-1 to 1)
     */
    public void drive(double forward, double right, double rotate) {
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backLeftPower = forward - right + rotate;
        double backRightPower = forward + right - rotate;

        // Scale all powers down together so none goes over 1.0
        double maxPower = 1.0;
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        frontLeft.setPower(frontLeftPower / maxPower);
        frontRight.setPower(frontRightPower / maxPower);
        backLeft.setPower(backLeftPower / maxPower);
        backRight.setPower(backRightPower / maxPower);
    }

    public void stop() {
        drive(0, 0, 0);
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Drive FL/FR", "%.2f / %.2f", frontLeft.getPower(), frontRight.getPower());
        telemetry.addData("Drive BL/BR", "%.2f / %.2f", backLeft.getPower(), backRight.getPower());
    }
}
