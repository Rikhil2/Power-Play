package org.firstinspires.ftc.teamcode.vision;

import static org.firstinspires.ftc.teamcode.vision.RunCamera.i;

import org.opencv.calib3d.StereoBM;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class MultipleWebcamPipelineLeft extends MultipleWebcamPipelineRight {
    Mat disparity = new Mat();
    @Override
    public Mat processFrame(Mat input) {
        while ((i%2) == 0) {}
        i++;
        //StereoBM stereo = StereoBM.create(16, 15);
        //stereo.compute(input, pipeline.passThrough, disparity);
        return passThrough;
    }
}
