package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class ImageStorer extends OpenCvPipeline {
    Telemetry  telemetry;
    ImageStorer(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    Mat gray = new Mat();
    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, gray, Imgproc.COLOR_BGR2GRAY);
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        Mat descriptors = new Mat();
        ORB orb = ORB.create(15);//test out different numbers for this; 15 seems to work well and prevent crashes
        orb.detectAndCompute(gray, new Mat(), keypoints, descriptors);
        KeyPoint[] keypointsArray = keypoints.toArray();
        Point[] pointsArray = new Point[keypointsArray.length];
        for (int i = 0; i < keypointsArray.length; i++) {
            pointsArray[i] = keypointsArray[i].pt;
            Imgproc.circle(input, pointsArray[i], 5, new Scalar(0, 0, 255), 2);
        }


//        Features2d.drawKeypoints(YCrCb, new MatOfKeyPoint(), YCrCb, new Scalar(0, 0, 255), Features2d.DrawMatchesFlags_DEFAULT);


        return input;
    }
}
