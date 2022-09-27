package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class MainPipeline extends OpenCvPipeline {
    Telemetry telemetry;
    public MainPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    List<MatOfPoint> yellowContours = new ArrayList<MatOfPoint>();
    double k = 1;
    List <MatOfPoint> yellowRect = new ArrayList<MatOfPoint>();
    List <Double> distance = new ArrayList<Double>();
    private Mat ycrcbMat       = new Mat();
    private Mat binaryMat      = new Mat();
    private Mat maskedInputMat = new Mat();
    Mat morphedThreshold = new Mat();
    Scalar RED = new Scalar(255, 0, 0);
    Scalar GREEN = new Scalar(0, 255, 0);

    Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15, 15));
    Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15, 15));

    void morphMask(Mat input, Mat output)
    {
        /*
         * Apply some erosion and dilation for noise reduction
         */

        Imgproc.erode(input, output, erodeElement);
        Imgproc.erode(output, output, erodeElement);

        Imgproc.dilate(output, output, dilateElement);
        Imgproc.dilate(output, output, dilateElement);
    }

    @Override
    public Mat processFrame(Mat input) {        Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(ycrcbMat, ycrcbMat, 2);
        Imgproc.threshold(ycrcbMat, binaryMat, 100, 110, Imgproc.THRESH_BINARY_INV);
        morphMask(binaryMat, morphedThreshold);

        yellowContours.clear();
        yellowRect.clear();

        Imgproc.findContours(morphedThreshold, yellowContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(input, yellowContours, -1, RED);

        for (int i = 0; i < yellowContours.size(); i++){
            for (int j = 0; j < 3; j++) {
                distance.add((double)yellowContours.get(i).height());
                telemetry.addData("yellow contour width: ", yellowContours.get(i).height());
                telemetry.update();
            }
        }

        return input;

    }
}
