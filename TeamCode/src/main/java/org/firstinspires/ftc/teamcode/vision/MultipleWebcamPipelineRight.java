package org.firstinspires.ftc.teamcode.vision;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class MultipleWebcamPipelineRight extends OpenCvPipeline {
    public Mat passThrough = new Mat();
    @Override
    public Mat processFrame(Mat input) {
        passThrough = input;
        return input;
    }
}
