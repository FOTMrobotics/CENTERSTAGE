package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class liftMotorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor L = hardwareMap.dcMotor.get("liftL");
        DcMotor R = hardwareMap.dcMotor.get("liftR");

        L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        L.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        L.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                L.setPower(gamepad1.left_trigger);
                R.setPower(gamepad1.right_trigger);
            } else {
                L.setPower(gamepad1.left_trigger);
                R.setPower(gamepad1.right_trigger);
            }

            telemetry.addData("left", L.getPower());
            telemetry.addData("right", R.getPower());
            telemetry.addData("leftPos", L.getCurrentPosition());
            telemetry.addData("rightPos", R.getCurrentPosition());

            telemetry.update();
        }
    }
}