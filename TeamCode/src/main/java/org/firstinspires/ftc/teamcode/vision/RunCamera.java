package org.firstinspires.ftc.teamcode.vision;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name="camera test", group="none")
public class RunCamera extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        OpenCvWebcam webcam;
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        MainPipeline myPipeline = new MainPipeline();
        webcam.setPipeline(myPipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(544, 288, OpenCvCameraRotation.UPRIGHT);
                FtcDashboard.getInstance().startCameraStream(webcam, 30);
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });

        int duckPosition = myPipeline.getDuckPosition();

        while (!isStarted() && !isStopRequested())
        {
            telemetry.addData("Realtime analysis ", myPipeline.getDuckPosition());
            telemetry.update();
            duckPosition = myPipeline.getDuckPosition();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }

        if (duckPosition == 0) {
            telemetry.addData("Duck position: ", "left");
            telemetry.update();
        } else if (duckPosition == 1) {
            telemetry.addData("Duck position: ", "middle");
            telemetry.update();
        } else if (duckPosition == 2) {
            telemetry.addData("Duck position: ", "right");
            telemetry.update();
        }

        sleep(10000);

        telemetry.addData("Program finished! ", "Prompt will remain on screen for 10 seconds then program will end.");
        telemetry.update();

        sleep(10000);

    }
}