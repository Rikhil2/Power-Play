package org.firstinspires.ftc.teamcode.vision;

import static org.firstinspires.ftc.teamcode.vision.RunCamera.i;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class MultipleWebcamPipelineRight extends OpenCvPipeline {
    public Mat passThrough;
    @Override
    public Mat processFrame(Mat input) {
        while ((i%2) == 1) {}
        passThrough = input;
        i++;
        return input;
    }
}
