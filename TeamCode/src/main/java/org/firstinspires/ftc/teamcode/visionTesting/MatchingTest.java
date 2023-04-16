package org.firstinspires.ftc.teamcode.visionTesting;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.features2d.ORB;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.openftc.easyopencv.OpenCvPipeline;

public class MatchingTest extends OpenCvPipeline{
    double fx = 822.317;
    double fy = 822.317;
    double cx = 319.495;
    double cy = 242.502;
    private Mat prevFrame;
    private Mat prevFeatures;
    private MatOfPoint2f prevPoints;
    Telemetry telemetry;
    public MatchingTest(Telemetry telemetry) {
        this.telemetry = telemetry;
        prevFrame = null;
        prevFeatures = new Mat();
        prevPoints = new MatOfPoint2f();
    }
    Mat cameraMatrix = new Mat(3,3, CvType.CV_32FC1);
    @Override
    public Mat processFrame(Mat input) {
        Mat gray = new Mat();
        Imgproc.cvtColor(input, gray, Imgproc.COLOR_BGR2GRAY);
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        Mat descriptors = new Mat();
        ORB orb = ORB.create(15);//test out different numbers for this; 15 seems to work well and prevent crashes
        orb.detectAndCompute(gray, new Mat(), keypoints, descriptors);
        KeyPoint[] keypointsArray = keypoints.toArray();
        Point[] pointsArray = new Point[keypointsArray.length];
        for (int i = 0; i < keypointsArray.length; i++) {
            pointsArray[i] = keypointsArray[i].pt;
        }
        if (prevFrame == null) {
            prevFrame = gray;
            prevFeatures = descriptors;
            prevPoints.fromArray(pointsArray);
            cameraMatrix.put(0,0, fx);
            cameraMatrix.put(0,1,0);
            cameraMatrix.put(0,2, cx);

            cameraMatrix.put(1,0,0);
            cameraMatrix.put(1,1,fy);
            cameraMatrix.put(1,2,cy);

            cameraMatrix.put(2, 0, 0);
            cameraMatrix.put(2,1,0);
            cameraMatrix.put(2,2,1);
            return input;
        }
        MatOfPoint2f currentPoints = new MatOfPoint2f();
        MatOfByte status = new MatOfByte();
        MatOfFloat err = new MatOfFloat();
        Video.calcOpticalFlowPyrLK(prevFrame, gray, prevPoints, currentPoints, status, err);
        MatOfKeyPoint currentKeypoints = new MatOfKeyPoint();
        int i = 0;
        for (Point p : currentPoints.toList()) {
            if (status.toArray()[i] != 0) {
                currentKeypoints.push_back(keypoints.row(i));
            }
            i++;
        }
        currentKeypoints.convertTo(currentKeypoints, CvType.CV_32FC1);
        Mat essential = Calib3d.findEssentialMat(prevPoints, currentPoints, cameraMatrix, Calib3d.RANSAC);
        Mat rotation = new Mat();
        Mat translation = new Mat();
        Calib3d.recoverPose(essential, prevPoints, currentPoints, rotation, translation);
        prevFrame = gray;
        prevFeatures = descriptors;
        prevPoints = currentPoints;

        // Print translation and rotation to console
        telemetry.addData("Translation", translation.dump());
        telemetry.addData("Rotation", rotation.dump());
        telemetry.update();

        return input;
    }
}