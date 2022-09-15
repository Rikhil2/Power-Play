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
        OpenCvWebcam webcam, webcam1, webcam2, webcam3;
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcamName = hardwareMap.get(WebcamName.class, "Webcam 2");
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcamName = hardwareMap.get(WebcamName.class, "Webcam 3");
        webcam2 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcamName = hardwareMap.get(WebcamName.class, "Webcam 4");
        webcam3 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        MainPipeline myPipeline = new MainPipeline();
//        webcam.setPipeline(myPipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(544, 288, OpenCvCameraRotation.UPRIGHT);
//                FtcDashboard.getInstance().startCameraStream(webcam, 30);
/*
                webcam1.startStreaming(544, 288, OpenCvCameraRotation.UPRIGHT);
                FtcDashboard.getInstance().startCameraStream(webcam1, 30);

                webcam2.startStreaming(544, 288, OpenCvCameraRotation.UPRIGHT);
                FtcDashboard.getInstance().startCameraStream(webcam2, 30);

                webcam3.startStreaming(544, 288, OpenCvCameraRotation.UPRIGHT);
                FtcDashboard.getInstance().startCameraStream(webcam3, 30);
*/
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });


        sleep(10000);

        telemetry.addData("Program finished! ", "Prompt will remain on screen for 10 seconds then program will end.");
        telemetry.update();

        sleep(10000);

    }
}