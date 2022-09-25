package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class MainPipeline extends OpenCvPipeline {
    public Scalar lower;
    public Scalar upper;
    public MainPipeline (double lowerR, double lowerG, double lowerB, double upperR, double upperG, double upperB) {
        lower = new Scalar(lowerR, lowerG, lowerB);
        upper = new Scalar(upperR, upperG, upperB);
    }
    private Mat ycrcbMat       = new Mat();
    private Mat binaryMat      = new Mat();
    private Mat maskedInputMat = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2YCrCb);
        Core.inRange(ycrcbMat, lower, upper, binaryMat);
        maskedInputMat.release();
        Core.bitwise_and(input, input, maskedInputMat, binaryMat);
        Scalar RED = new Scalar(255, 0, 0);
        return maskedInputMat;
    }
}
