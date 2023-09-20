package org.firstinspires.ftc.teamcode.vision.old;

import com.acmerobotics.dashboard.config.Config;

import org.opencv.core.Mat;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepthMapPipeline extends OpenCvPipeline {
    //currently just for contours
    double distance;
    Mat gray = new Mat();
    Mat edgeDetectorFrame = new Mat();
    Mat hierarchey = new Mat();
    List<MatOfPoint> contours = new ArrayList<>();
    double bottomThreshold = 100;
    double topThreshold = 300;
    Scalar blue = new Scalar(0, 0, 255);

    public boolean filterContours(MatOfPoint contour2) {
        return Imgproc.contourArea(contour2) > 100;
    }

    MatOfPoint biggestContour = new MatOfPoint();

    int contourCount = 0;

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, gray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(gray, edgeDetectorFrame, bottomThreshold, topThreshold);
        //TODO determine pixel width of poles in camera, start testing then do inverse square regression for distance
        contours.clear();
        Imgproc.findContours(edgeDetectorFrame, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(input, contours, -1, blue);
        return input;
    }
}
