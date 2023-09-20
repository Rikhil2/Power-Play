package org.firstinspires.ftc.teamcode.visionTesting;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PropDetectionRed extends OpenCvPipeline {
    Telemetry telemetry;
    public PropDetectionRed(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    Mat YCRCB = new Mat();
    Mat maskRed = new Mat();
    Size kSize = new Size(5, 5);
    Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kSize);
    public static Scalar lowRed = new Scalar(0, 150, 80.8);
    public static Scalar highRed = new Scalar(255, 255, 138.8);
    public List<MatOfPoint> redContours = new ArrayList<>();
    public List<MatOfPoint> redContoursSorted = new ArrayList<>();
    public MatOfPoint biggestContour = new MatOfPoint();
    public static int areaThresh = 100;
    Rect right = new Rect();
    Rect middle = new Rect();
    Rect left = new Rect();
    public boolean filterContours(MatOfPoint contour) {
        return Imgproc.contourArea(contour) > areaThresh;
    }
    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, YCRCB, Imgproc.COLOR_RGB2YCrCb);
        Imgproc.erode(YCRCB, YCRCB, kernel);
        Imgproc.dilate(YCRCB, YCRCB, kernel);

/* for debugging/finding the right threshold
        double zero = 0;
        double one = 0;
        double two = 0;
        for (int i = 0; i<100; i++) {
            for (int j = 0; j<100; j++) {
                zero += YCRCB.get(i, j)[0];
            }
        }
        for (int i = 0; i<100; i++) {
            for (int j = 0; j<100; j++) {
                one += YCRCB.get(i, j)[1];
            }
        }
        for (int i = 0; i<100; i++) {
            for (int j = 0; j<100; j++) {
                two += YCRCB.get(i, j)[2];
            }
        }
        telemetry.addData("zero chanel average", zero/(10000*1));
        telemetry.addData("one chanel average", one/(10000*1));
        telemetry.addData("two chanel average", two/(10000*1));
        telemetry.update();
        Imgproc.rectangle(YCRCB, new org.opencv.core.Point(0, 0), new org.opencv.core.Point(100, 100), new Scalar(0, 0, 255), 3);
//*/

        // Finds the pixels within the thresholds and puts them in the mat object "maskRed"
        Core.inRange(YCRCB, lowRed, highRed, maskRed);

        redContours.clear();
        redContoursSorted.clear();

        Imgproc.findContours(maskRed, redContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        for (int i = 0; i < redContours.size(); i++)
            if (!filterContours(redContours.get(i)))
                redContours.remove(redContours.get(i));

        if (redContours.size() < 3) {return maskRed;}

        for (int i = 0; i < 3; i++) {
            biggestContour = Collections.max(redContours, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).width));
            redContoursSorted.add(biggestContour);
            redContours.remove(biggestContour);
        }

        right = Imgproc.boundingRect(redContoursSorted.get(0));
        middle = Imgproc.boundingRect(redContoursSorted.get(1));
        left = Imgproc.boundingRect(redContoursSorted.get(2));

        ArrayList<Rect> tempRects = new ArrayList<>();
        tempRects.add(right);tempRects.add(middle);tempRects.add(left);
        right = Collections.max(tempRects, Comparator.comparingDouble(t0 -> t0.x));
        tempRects.remove(right);
        left = Collections.min(tempRects, Comparator.comparingDouble(t0 -> t0.x));
        tempRects.remove(left);
        middle = tempRects.get(0);

        left = new Rect(new Point(left.x, left.y), new Point(left.x+left.width, 0));
        middle = new Rect(new Point(middle.x, middle.y), new Point(middle.x+middle.width, 0));
        right = new Rect(new Point(right.x, right.y), new Point(right.x+right.width, 0));

        telemetry.update();
        return input;
    }
}
