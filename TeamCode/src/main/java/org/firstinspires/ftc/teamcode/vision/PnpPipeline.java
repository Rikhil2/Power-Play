package org.firstinspires.ftc.teamcode.vision;

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
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.List;

public class PnpPipeline extends OpenCvPipeline {
    Telemetry  telemetry;
    PnpPipeline (Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    Mat cameraMatrix = new Mat(3,3,CvType.CV_32FC1);
    int i = 0;
    Point3[] arrayPoints3d = new Point3[4];
    MatOfPoint3f points3d;
    MatOfPoint2f points2d;

    public List<MatOfPoint> redContours;
    public List<MatOfPoint> iterator;
    Mat YCrCb = new Mat();
    Mat contourMat = new Mat();
    Scalar lower = new Scalar(0, 0, 0);
    Scalar upper = new Scalar(0,0,0);//need to tune to red cones
    Point endpoint1, endpoint2;
    Point checkEndpoint1, checkEndpoint2;
    Point corner1, corner2, corner3, corner4;

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.inRange(YCrCb, lower, upper, YCrCb);
        Imgproc.cvtColor(YCrCb, contourMat, Imgproc.COLOR_YCrCb2RGB);
        YCrCb.release();
        Imgproc.findContours(contourMat, redContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        contourMat.release();

        for (MatOfPoint contour: redContours) {
            //get first and last points
            endpoint1 = new Point(contour.get(0, 0));
            endpoint2 = new Point(contour.get((contour.rows()), (contour.cols())));

            if (endpoint1 == checkEndpoint1 || endpoint1 == checkEndpoint2){
                //endpoint 1 thus equals a distinct corner
                if (corner1 == null)
                    corner1 = endpoint1;
                else if (corner2 == null)
                    corner2 = endpoint1;
                else if (corner3 == null)
                    corner3 = endpoint1;
                else if (corner4 == null)
                    corner4 = endpoint1;
            }

            if (endpoint2 == checkEndpoint1 || endpoint2 == checkEndpoint2) {
                //thus endpoint 2 is a distinct corner
                if (corner1 == null)
                    corner1 = endpoint2;
                else if (corner2 == null)
                    corner2 = endpoint2;
                else if (corner3 == null)
                    corner3 = endpoint2;
                else if (corner4 == null)
                    corner4 = endpoint2;
            }

            checkEndpoint1 = endpoint1;
            checkEndpoint2 = endpoint2;

            //how to filter 8 corners into the 4 I want???

        }

        redContours.clear();
        iterator.clear();

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

        Point[] points = {corner1, corner2, corner3, corner4};

        points3d = new MatOfPoint3f(arrayPoints3d);
        points2d = new MatOfPoint2f(points);
        Pose pose = new Pose();
        Calib3d.solvePnP(points3d, points2d, cameraMatrix, new MatOfDouble(), pose.rvec, pose.tvec, false);
        telemetry.addLine(String.format("Translation X: %.2f feet", pose.tvec.get(0, 0)));//complete guess i have no idea how it goes into the matrix
        return input;
    }

    public static class Pose
    {
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
