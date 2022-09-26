package org.firstinspires.ftc.teamcode.visionTesting;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestPipeline extends OpenCvPipeline {

    Telemetry telemetry;
    List<MatOfPoint> yellowContours = new ArrayList<MatOfPoint>();

    Rect YellowRect = new Rect();
    List <Rect> yellowRect = new ArrayList<Rect>();

    public TestPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    public Scalar lower = new Scalar(0, 0, 0);;
    public Scalar upper = new Scalar(255, 255, 255);

    int minSize = 100;

    MatOfPoint biggestContour = new MatOfPoint();

    private Mat ycrcbMat       = new Mat();
    private Mat binaryMat      = new Mat();
    private Mat maskedInputMat = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2YCrCb);
        Core.inRange(ycrcbMat, lower, upper, binaryMat);
        maskedInputMat.release();
        Core.bitwise_and(input, input, maskedInputMat, binaryMat);
        Scalar RED = new Scalar(255, 0, 0);

        Imgproc.findContours(maskedInputMat, yellowContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(input, yellowContours, -1, RED);

        for (int i = 0; i < yellowContours.size(); i++) {
            if (Imgproc.contourArea(yellowContours.get(i)) < minSize) {
                biggestContour = Collections.max(yellowContours, (t0, t1) -> {
                    return Double.compare(Imgproc.boundingRect(t0).width, Imgproc.boundingRect(t1).width);
                });
                YellowRect = Imgproc.boundingRect(biggestContour);
                yellowRect.add(Imgproc.boundingRect(yellowContours.get(i)));
            }
        }
        return maskedInputMat;
    }
}