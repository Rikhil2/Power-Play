package org.firstinspires.ftc.teamcode.vision.old;

import org.opencv.core.Mat;

public class MultipleWebcamPipelineLeft extends MultipleWebcamPipelineRight {
    Mat disparity = new Mat();
    @Override
    public Mat processFrame(Mat input) {
        //intent is to return the same Mat as the other pipeline, then work up from there

        while (passThrough == null) {}

        return passThrough;

        //StereoBM stereo = StereoBM.create(16, 15);
        //stereo.compute(input, passThrough, disparity);
        //return disparity;
    }
}
