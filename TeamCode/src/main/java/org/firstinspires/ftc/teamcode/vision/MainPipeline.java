package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class MainPipeline extends OpenCvPipeline {
    public Scalar lower = new Scalar(66.6, 140.3, 0);
    public Scalar upper = new Scalar(225.3, 255, 92.1);

    int duckpos = 2;//left=0; middle=1; right=2;

    private Mat ycrcbMat       = new Mat();
    private Mat binaryMat      = new Mat();
    private Mat maskedInputMat = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2YCrCb);
        Core.inRange(ycrcbMat, lower, upper, binaryMat);
        maskedInputMat.release();
        Core.bitwise_and(input, input, maskedInputMat, binaryMat);
        Scalar GREEN = new Scalar(0, 255, 0);
        Scalar BLUE = new Scalar(0, 0, 255);
        Scalar RED = new Scalar(255, 0, 0);

        return maskedInputMat;
    }
}
