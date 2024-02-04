package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.AnalogInput;

@TeleOp
public class ServoTesting extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Servo servo1 = hardwareMap.servo.get("servo1");
        AnalogInput analogInput = hardwareMap.get(AnalogInput.class, "analog");

        double setposition = 0;
        double position;
        boolean pressed = false;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (!pressed) {
                if (gamepad1.dpad_up) {
                    setposition = setposition + 0.01;
                } else if (gamepad1.dpad_down) {
                    setposition = setposition - 0.01;
                } else if (gamepad1.a) {
                    setposition = 0;
                }
            }

            pressed = gamepad1.dpad_up || gamepad1.dpad_down;

            servo1.setPosition(setposition);

            position = analogInput.getVoltage() / 3.3 * 360;

            telemetry.addData("Position",  setposition);
            telemetry.addData("Servo Position", position);

            telemetry.update();
        }
    }
}