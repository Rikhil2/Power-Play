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
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomographyTesting2 extends OpenCvPipeline {
    public static Scalar lowerRed1 = new Scalar(116.2, 204, 177.1);
    public static Scalar upperRed1 = new Scalar(117.6, 255, 255);
    public static Scalar lowerRed2 = new Scalar(113.3, 208.3, 177.1);
    public static Scalar upperRed2 = new Scalar(119, 255, 255);
    Mat mask1 = new Mat();
    Mat mask2 = new Mat();
    Mat mask = new Mat();
    List<MatOfPoint> contours = new ArrayList<>();
    Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
    Mat cameraMatrix = new Mat(3,3, CvType.CV_32FC1);
    int i = 0;
    Point3[] arrayPoints3d = new Point3[4];
    MatOfPoint3f points3d;
    MatOfPoint2f points2d;


    @Override
    public Mat processFrame(Mat input) {
        if (i == 0) {
            cameraMatrix.put(0,0, 822.317);
            cameraMatrix.put(0,1,0);
            cameraMatrix.put(0,2, 319.495);

            cameraMatrix.put(1,0,0);
            cameraMatrix.put(1,1,822.317);
            cameraMatrix.put(1,2,242.502);

            cameraMatrix.put(2, 0, 0);
            cameraMatrix.put(2,1,0);
            cameraMatrix.put(2,2,1);

            arrayPoints3d[0] = new Point3(1, 2.5, 0);
            arrayPoints3d[1] = new Point3(-1, 2.5, 0);
            arrayPoints3d[2] = new Point3(-2, -1.58439329, 0);
            arrayPoints3d[3] = new Point3(-2, -1.5439329, 0);

            i++;
        }
        contours.clear();
        Mat hsvImage = new Mat();
        Imgproc.cvtColor(input, hsvImage, Imgproc.COLOR_BGR2HSV);
        Core.inRange(hsvImage, lowerRed1, upperRed1, mask1);
        Core.inRange(hsvImage, lowerRed2, upperRed2, mask2);
        Core.bitwise_or(mask1, mask2, mask);
        Imgproc.erode(mask, mask, kernel);
        Imgproc.dilate(mask, mask, kernel);
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            double epsilon = 0.01 * Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true);
            Imgproc.approxPolyDP(new MatOfPoint2f(contour.toArray()), approxCurve, epsilon, true);
            if (approxCurve.total() == 4 && Imgproc.contourArea(approxCurve) > 100) {
                System.out.println(approxCurve.total());
                // Draw the contours
//                Imgproc.drawContours(input, contours, -1, new Scalar(0, 0, 255), 2);

                // Draw the bounding box
                Rect boundingRect = Imgproc.boundingRect(contour);
                Imgproc.rectangle(input, boundingRect.tl(), boundingRect.br(), new Scalar(0, 255, 0), 2);

                // Extract the corners
                Point[] corners = approxCurve.toArray();
                for (Point corner : corners) {
                    System.out.println(corner);
                    Imgproc.circle(input, corner, 4, new Scalar(255, 0, 0));
                }
                points3d = new MatOfPoint3f(arrayPoints3d);
                points2d = new MatOfPoint2f(corners);
                Pose pose = new Pose();
                Calib3d.solvePnP(points3d, points2d, cameraMatrix, new MatOfDouble(), pose.rvec, pose.tvec, false);
                System.out.println(Arrays.toString(pose.tvec.get(0, 0)) + " feet"); System.out.println(Arrays.toString(pose.rvec.get(0, 0)));
            }
        }
        hierarchy.release();
        hsvImage.release();

        return input;
    }


    public static class Pose {
        Mat rvec;
        Mat tvec;

        public Pose()
        {
            rvec = new Mat();
            tvec = new Mat();
        }

        public Pose(Mat rvec, Mat tvec)
        {
            this.rvec = rvec;
            this.tvec = tvec;
        }
    }
}
