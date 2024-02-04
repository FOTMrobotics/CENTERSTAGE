package org.firstinspires.ftc.teamcode.Auto;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.Roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.Subsystems.Lift;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class BlueAutoNOBackdrop extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Intake intake = new Intake(hardwareMap);
        Lift lift = new Lift(hardwareMap);
        Bucket bucket = new Bucket(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        int waitTime = 500;
        OpenCvWebcam webcam;
        TeamElementDetectionBlue2 pipeline;

        Pose2d start = new Pose2d(-39.5, 62, Math.toRadians(90));

        drive.setPoseEstimate(start);

        TrajectorySequence toTape = drive.trajectorySequenceBuilder(start)
                .lineToLinearHeading(new Pose2d(-36, 36, Math.toRadians(90)))
                .build();

        TrajectorySequence placeLeftTape = drive.trajectorySequenceBuilder(start)
                .lineToLinearHeading(new Pose2d(-36, 36, Math.toRadians(90)))
                .turn(-Math.toRadians(90))
                .build();

        TrajectorySequence placeCenterTape = drive.trajectorySequenceBuilder(start)
                .lineToLinearHeading(new Pose2d(-52, 23.5, Math.toRadians(0)))
                .build();

        TrajectorySequence placeRightTape = drive.trajectorySequenceBuilder(start)
                .lineToLinearHeading(new Pose2d(-36, 36, Math.toRadians(90)))
                .turn(Math.toRadians(90))
                .build();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new TeamElementDetectionBlue2();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(640,360, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });

        int pos = pipeline.getPosition();

        while (!isStarted() && !isStopRequested())
        {
            pos = pipeline.getPosition();
            if (pos == 1) {
                telemetry.addLine("LEFT");
            } else if (pos == 2) {
                telemetry.addLine("CENTER");
            } else if (pos == 3) {
                telemetry.addLine("RIGHT");
            } else {
                telemetry.addLine("not detect :(");
            }
            telemetry.addData("leftAvg", pipeline.getLeftAvgFinal());
            telemetry.addData("rightAvg", pipeline.getRightAvgFinal());
            telemetry.update();
            sleep(100);
        }

        Pose2d endPos = toTape.end();
        if (pos == 1) {
            drive.followTrajectorySequence(placeLeftTape);
            endPos = placeLeftTape.end();
        } else if (pos == 2) {
            drive.followTrajectorySequence(placeCenterTape);
            endPos = placeCenterTape.end();
        } else if (pos == 3) {
            drive.followTrajectorySequence(placeRightTape);
            endPos = placeRightTape.end();
        } else {
            drive.followTrajectorySequence(toTape);
        }
        intake.out(1);
        sleep(100);
        intake.out(0.55);
        sleep(1500);
        intake.stop();
        sleep(waitTime);

        TrajectorySequence toBoard1 = drive.trajectorySequenceBuilder(endPos)
                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(0)))
                .turn(Math.toRadians(180))
                .lineToLinearHeading(new Pose2d(60, 12, Math.toRadians(180)))
                .build();

        TrajectorySequence toBoard2 = drive.trajectorySequenceBuilder(endPos)
                .lineToLinearHeading(new Pose2d(-52, 12, Math.toRadians(0)))
                .turn(Math.toRadians(180))
                .lineToLinearHeading(new Pose2d(60, 12, Math.toRadians(180)))
                .build();

        TrajectorySequence toBoard3 = drive.trajectorySequenceBuilder(endPos)
                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(60, 12, Math.toRadians(180)))
                .build();

        if (pos == 1) {
            drive.followTrajectorySequence(toBoard1);
        } else if (pos == 2) {
            drive.followTrajectorySequence(toBoard2);
        } else {
            drive.followTrajectorySequence(toBoard3);
        }
    }
}

class TeamElementDetectionBlue2 extends OpenCvPipeline {
    Mat YCbCr = new Mat();
    Mat leftCrop;
    Mat rightCrop;
    double leftAvgFinal;
    double rightAvgFinal;
    Scalar rectColor = new Scalar (255.0, 0.0, 0);
    Mat output = new Mat();
    int pos = 0;

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);

        // 640,360
        Rect leftRect = new Rect(100, 130, 200, 160);
        Rect rightRect = new Rect(399, 130, 200, 160);

        input.copyTo(output);
        Imgproc.rectangle(output, leftRect, rectColor, 2);
        Imgproc.rectangle(output, rightRect, rectColor, 2);

        leftCrop = YCbCr.submat(leftRect);
        rightCrop = YCbCr.submat(rightRect);

        // Channel 0 = Luma, Channel 1 = Cb (Use for red), Channel 2 = Cr (Use for blue)
        Core.extractChannel(leftCrop, leftCrop, 2);
        Core.extractChannel(rightCrop, rightCrop, 2);

        Scalar leftAvg = Core.mean(leftCrop);
        Scalar rightAvg = Core.mean(rightCrop);

        // Make sure each of these values are equal when no object is present.
        leftAvgFinal = leftAvg.val[0];
        rightAvgFinal = rightAvg.val[0] - 8;

        if (rightAvgFinal > leftAvgFinal) {
            pos = 3;
        } else if (rightAvgFinal - leftAvgFinal < 9 && rightAvgFinal - leftAvgFinal > -9) {
            pos = 1;
        } else {
            pos = 2;
        }

        return (output);
    }

    public int getPosition() {
        return pos;
    }

    public double getLeftAvgFinal() {return leftAvgFinal;}

    public double getRightAvgFinal() {return rightAvgFinal;}
}
