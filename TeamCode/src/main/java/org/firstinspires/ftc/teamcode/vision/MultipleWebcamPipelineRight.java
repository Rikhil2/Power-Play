package org.firstinspires.ftc.teamcode.vision;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class MultipleWebcamPipelineRight extends OpenCvPipeline {
    public Mat passThrough;
    @Override
    public Mat processFrame(Mat input) {
        while (passThrough == null) {
            if (input != null) {
                passThrough = input;
            }
        }
        return input;
    }
}
