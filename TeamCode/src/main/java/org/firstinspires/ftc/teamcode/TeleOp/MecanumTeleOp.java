package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Lift;

@TeleOp
public class MecanumTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("backRight");
        Intake intake = new Intake(hardwareMap);
        Bucket bucket = new Bucket(hardwareMap);
        Lift lift = new Lift(hardwareMap);
        Servo plane = hardwareMap.servo.get("plane");
        plane.setPosition(1);

        boolean pressed = false;

        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double multiplier = (Boolean.compare(gamepad1.right_bumper, false) * 2) + 1; // For slow mode
            double frontLeftPower = (y + x + rx) / denominator / multiplier;
            double backLeftPower = (y - x + rx) / denominator / multiplier;
            double frontRightPower = (y - x - rx) / denominator / multiplier;
            double backRightPower = (y + x - rx) / denominator / multiplier;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            telemetry.addData("liftL", lift.getLeftPos());
            telemetry.addData("liftR", lift.getRightPos());
            telemetry.addData("liftLTarget", lift.getLeftTargetPos());
            telemetry.addData("liftRTarget", lift.getRightTargetPos());
            telemetry.addData("leftfront", motorFrontLeft.getCurrentPosition());
            telemetry.addData("rightfront", motorFrontRight.getCurrentPosition());
            telemetry.addData("leftback", motorBackLeft.getCurrentPosition());
            telemetry.addData("rightback", motorBackRight.getCurrentPosition());

            telemetry.update();

            lift.runUsingController(gamepad2.left_trigger, gamepad2.right_trigger);

            //bucket.moveUsingLift(lift.getLeftPos());

            bucket.runArm(gamepad2.x);

            intake.run(gamepad2.a, gamepad2.b);

            if (!pressed) {
                if (gamepad1.a) {
                    bucket.release();
                    sleep(500);
                    bucket.hold();
                }
            }

            pressed = gamepad1.a;

            if (gamepad1.x) {
                plane.setPosition(0);
            } else {
                plane.setPosition(1);
            }
        }
    }
}