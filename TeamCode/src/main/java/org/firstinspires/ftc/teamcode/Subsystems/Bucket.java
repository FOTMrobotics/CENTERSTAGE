package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Bucket {
    private Servo pivot, drop, arm;
    boolean armToggle = false;
    boolean dropToggle = false;
    boolean pressed1 = false;
    boolean pressed2 = false;

    public Bucket(HardwareMap hardwareMap) {
        pivot = hardwareMap.get(Servo.class, "pivot");
        drop = hardwareMap.get(Servo.class, "drop");
        arm = hardwareMap.get(Servo.class, "arm");

        arm.setPosition(0);
        pivot.setPosition(0.05);
        // 0.69 for drop pos 4 pivot
        drop.setPosition(1);
    }

    public void moveUsingLift(int liftPos) {
        if (liftPos > 250 && liftPos < 450) {
            pivot.setPosition(1 - (0.35 * ((double) (liftPos - 250) / 200)));
        }
    }

    public void runArm(boolean button) {
        if (!pressed1) {
            if (button) {
                if (armToggle) {
                    armIn();
                    armToggle = false;
                } else {
                    armOut();
                    armToggle = true;
                }
            }
        }

        pressed1 = button;
    }

    public void armOut() {
        arm.setPosition(0.45);
        for (int i = 10; i > 0; i--) pivot.setPosition(0.69 / i); // Goes to 69
    }

    public void armIn() {
        arm.setPosition(0);
        for (int i = 10; i > 0; i--) pivot.setPosition(0.74 - (0.69 / i)); // Goes to 0
    }

    public void hold() {drop.setPosition(1);}

    public void release() {drop.setPosition(0.57);}

    public void dropPos() {pivot.setPosition(0.75);}

    public void pickUpPos() {pivot.setPosition(0);
    }
}
