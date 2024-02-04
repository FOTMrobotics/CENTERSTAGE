package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Lift {
    private DcMotor liftL, liftR;

    boolean braking = false;
    int liftPos = 0;
    int liftMaxSpeed = 40;

    public Lift(HardwareMap hardwareMap) {
        liftL = hardwareMap.get(DcMotor.class, "liftL");
        liftR = hardwareMap.get(DcMotor.class, "liftR");

        liftL.setDirection(DcMotorSimple.Direction.REVERSE);

        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void runToPos(int pos) {
        liftL.setTargetPosition(pos);
        liftR.setTargetPosition(pos);
        liftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //liftL.setPower(Boolean.compare(getLeftPos() > 10 && getLeftTargetPos() != 0, false));
        //liftR.setPower(Boolean.compare(getLeftPos() > 10 && getLeftTargetPos() != 0, false));
        liftL.setPower(1);
        liftR.setPower(1);
    }

    public void runUsingController(double leftTrigger, double rightTrigger) {
        if (leftTrigger > 0) {
            liftPos -= (int) (leftTrigger * liftMaxSpeed);
        } else if (rightTrigger > 0) {
            liftPos += (int) (rightTrigger * liftMaxSpeed);
        }

        if (liftPos < 0) {
            liftPos = 0;
        } else if (liftPos > 2750) {
            liftPos = 2750;
        }

        //if (getLeftPos() > liftPos - 5 && getLeftPos() < liftPos + 5) {runToPos(liftPos);}
        runToPos(liftPos);
    }

    public void toZero() {
        liftL.setTargetPosition(0);
        liftR.setTargetPosition(0);
        liftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftL.setPower(1);
        liftR.setPower(1);
    }

    public int getLeftPos() {
        return liftL.getCurrentPosition();
    }

    public int getRightPos() {
        return liftR.getCurrentPosition();
    }

    public int getLeftTargetPos() {
        return liftL.getTargetPosition();
    }

    public int getRightTargetPos() {
        return liftR.getTargetPosition();
    }

    public double[] getPower() {
        return new double[]{liftL.getPower(), liftR.getPower()};
    }
}
