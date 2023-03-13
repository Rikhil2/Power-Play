package org.firstinspires.ftc.teamcode.visionTesting;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagDetectorJNI;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestPipeline extends OpenCvPipeline {

    public static int threshold = 800;
    public static int lowX = 430;
    public static int highX = 470;

    static final Scalar RED = new Scalar(255, 0, 0);

    public Rect YellowRect = new Rect();

    public Point YellowCenter = new Point();

    public List<MatOfPoint> yellowContours = new ArrayList<>();

    public MatOfPoint biggestContour = new MatOfPoint();

    public List<MatOfPoint> yellowContoursSorted = new ArrayList<>();

    public boolean filterContours(MatOfPoint contour) {
        return Imgproc.contourArea(contour) > threshold;
    }

    Scalar lowYellow = new Scalar(10, 125, 150); //10, 100, 50
    Scalar highYellow = new Scalar(35, 255, 255); //35, 255, 255

    // Mat object for the yellow mask
    Mat maskYellow = new Mat();

    // Mat object for HSV color space
    Mat HSV = new Mat();

    // Kernel size for blurring
    Size kSize = new Size(5, 5);
    Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kSize);
    Mat upper = new Mat();
    Mat lower = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        upper = input.submat(0, input.rows()/2, 0, input.cols());
        lower = input.submat(input.rows()/2, input.rows(), 0, input.cols());

        Imgproc.cvtColor(upper, HSV, Imgproc.COLOR_RGB2HSV);
        Imgproc.erode(HSV, HSV, kernel);

        // Finds the pixels within the thresholds and puts them in the mat object "maskYellow"
        Core.inRange(HSV, lowYellow, highYellow, maskYellow);

        // Clears the arraylists
        yellowContours.clear();

        // Finds the contours and draws them on the screen
        Imgproc.findContours(maskYellow, yellowContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        // Iterates through each contour
        for (int i = 0; i < yellowContours.size(); i++)
            if (filterContours(yellowContours.get(i)))
                yellowContoursSorted.add(yellowContours.get(i));
        if (yellowContoursSorted.size() == 0) {
            YellowCenter = new Point(0, 0);
        } else {
            biggestContour = Collections.max(yellowContoursSorted, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).width));
            YellowRect = Imgproc.boundingRect(biggestContour);
            YellowCenter = (new Point(YellowRect.x + (double)(YellowRect.width)/2, YellowRect.y + (double)(YellowRect.height)/2));
            Imgproc.circle(input, YellowCenter, 5, new Scalar(0, 255, 0), -1);
        }

        yellowContoursSorted.clear();

        maskYellow.release();

        HSV.release();

        Imgproc.cvtColor(lower, HSV, Imgproc.COLOR_RGB2HSV);
        Imgproc.erode(HSV, HSV, kernel);

        // Finds the pixels within the thresholds and puts them in the mat object "maskYellow"
        Core.inRange(HSV, lowYellow, highYellow, maskYellow);

        // Clears the arraylists
        yellowContours.clear();

        // Finds the contours and draws them on the screen
        Imgproc.findContours(maskYellow, yellowContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        // Iterates through each contour
        for (int i = 0; i < yellowContours.size(); i++)
            if (filterContours(yellowContours.get(i)))
                yellowContoursSorted.add(yellowContours.get(i));
        if (yellowContoursSorted.size() == 0) {
            YellowCenter = new Point(0, 0);
        } else {
            biggestContour = Collections.max(yellowContoursSorted, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).width));
            YellowRect = Imgproc.boundingRect(biggestContour);
            YellowCenter = (new Point(YellowRect.x + (double)(YellowRect.width)/2, YellowRect.y + (double)(YellowRect.height)/2));
            Imgproc.circle(input, new Point(YellowCenter.x, YellowCenter.y + ((double)input.rows())/2), 5, new Scalar(0, 0, 255), -1);
        }

        yellowContoursSorted.clear();

        maskYellow.release();

        HSV.release();









        Imgproc.cvtColor(input, HSV, Imgproc.COLOR_RGB2HSV);
        Imgproc.erode(HSV, HSV, kernel);

        // Finds the pixels within the thresholds and puts them in the mat object "maskYellow"
        Core.inRange(HSV, lowYellow, highYellow, maskYellow);

        // Clears the arraylists
        yellowContours.clear();

        // Finds the contours and draws them on the screen
        Imgproc.findContours(maskYellow, yellowContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        // Iterates through each contour
        for (int i = 0; i < yellowContours.size(); i++)
            if (filterContours(yellowContours.get(i)))
                yellowContoursSorted.add(yellowContours.get(i));
        if (yellowContoursSorted.size() == 0) {
            YellowCenter = new Point(0, 0);
            return input;
        } else {
//            Imgproc.drawContours(input, yellowContoursSorted, -1, RED);
            biggestContour = Collections.max(yellowContoursSorted, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).width));
            YellowRect = Imgproc.boundingRect(biggestContour);
//            Imgproc.rectangle(input, YellowRect, RED, 2);
            YellowCenter = (new Point(YellowRect.x + (double)(YellowRect.width)/2, YellowRect.y + (double)(YellowRect.height)/2));
            Imgproc.circle(input, YellowCenter, 5, RED, -1);

        }

        yellowContoursSorted.clear();

        maskYellow.release();

        HSV.release();
        return input;
    }
}