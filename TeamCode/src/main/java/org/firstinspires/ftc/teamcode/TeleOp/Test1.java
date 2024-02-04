package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Test1 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Servo servo1 = hardwareMap.servo.get("servo1");

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (gamepad1.a) {
                servo1.setPosition(0);
            } else if (gamepad1.b) {
                servo1.setPosition(0.55); // 0.55 is horizontal
            } else if (gamepad1.y) {
                servo1.setPosition(1);
            }

            telemetry.addData("Position",  servo1.getPosition());

            telemetry.update();
        }
    }
}