package org.firstinspires.ftc.teamcode.vision;

import org.opencv.core.Mat;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class DepthMapPipeline extends OpenCvPipeline {
    //currently just for contours
    double distance;
    Mat gray = new Mat();
    Mat edgeDetectorFrame = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, gray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(gray, edgeDetectorFrame, new Size(3, 3));
        Imgproc.Canny(edgeDetectorFrame, edgeDetectorFrame, 100, 100*3);
        //TODO determine pixel width of poles in camera, start testing then do inverse square regression for distance
        return edgeDetectorFrame;
    }
}
