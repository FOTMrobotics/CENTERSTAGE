package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotor intake;

    public Intake(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void run(boolean a, boolean b) {
        if (a) {
            in(1);
        } else if (b) {
            out(1);
        } else {
            stop();
        }
    }

    public void in(double power) {
        intake.setPower(power);
    }

    public void out(double power) {
        intake.setPower(-power);
    }

    public void stop() {
        intake.setPower(0);
    }

    public double getPower() {
        return intake.getPower();
    }
}
