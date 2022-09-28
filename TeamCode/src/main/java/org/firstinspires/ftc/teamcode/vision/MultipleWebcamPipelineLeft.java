package org.firstinspires.ftc.teamcode.vision;

import org.opencv.calib3d.StereoBM;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class MultipleWebcamPipelineLeft extends OpenCvPipeline {
    Mat disparity = new Mat();
    MultipleWebcamPipelineRight pipeline = new MultipleWebcamPipelineRight();
    @Override
    public Mat processFrame(Mat input) {
        StereoBM stereo = StereoBM.create(16, 15);
        stereo.compute(input, pipeline.passThrough, disparity);
        return disparity;
    }
}
