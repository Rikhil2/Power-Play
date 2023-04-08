package org.firstinspires.ftc.teamcode.visionTesting;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class Iteration extends OpenCvPipeline {
    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2RGBA);
        Core.inRange(input, new Scalar(0, 0, 0, 0), new Scalar(255, 255, 255, 255), input);
        return input;
    }
}
