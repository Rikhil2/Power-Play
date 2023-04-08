package org.firstinspires.ftc.teamcode.visionTesting;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class SleevePipeline extends OpenCvPipeline {
    Point pointA = new Point(0, 0);
    Point pointB = new Point(100, 100);
    int tag;
    Telemetry telemetry;
    public SleevePipeline (Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    @Override
    public Mat processFrame(Mat input) {
        Mat sleeveArea = input.submat(new Rect(pointA, pointB));
        Scalar sumColors = Core.sumElems(sleeveArea);
        double minColor = Math.min(sumColors.val[0], Math.min(sumColors.val[1], sumColors.val[2]));
        if (sumColors.val[0] == minColor) {
            tag = 1;//blueness
        } else if (sumColors.val[1] == minColor) {
            tag = 2;//redness
        } else {
            tag = 0;//yellowness
        }
        Imgproc.rectangle(input, pointA, pointB, new Scalar(0, 0, 255), 2);
        telemetry.addData("tag: ", tag);
        telemetry.update();
        return input;
    }
}
