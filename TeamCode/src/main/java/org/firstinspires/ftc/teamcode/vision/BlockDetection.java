/*
The idea behind this pipeline is that a block
in a given position on the screen will have a
corresponding position relative to the camera in real life.

Thus, we can figure out the position where the block's center
should be on the screen for the block to be right in front of the robot.

It takes the size of the countour(size of the block on the screen) and stops the robot when the contour is biggest.

This pipeline is not in use but most likely I will use a
PID controller to loop the feedback from the camera to the robot speed.
 */

package org.firstinspires.ftc.teamcode.vision;
import static org.opencv.core.Core.inRange;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockDetection extends OpenCvPipeline {
    Telemetry telemetry;

    static final Scalar GOLD = new Scalar(255, 215, 0);

    boolean YELLOW = true;

    public int yellowContourCount = 0;

    public List<Rect> yellowRect;

    public Rect YellowRect;

    public List<MatOfPoint> yellowContours;

    public MatOfPoint biggestContour;
    public BlockDetection(Telemetry telemetry) {
        yellowContours = new ArrayList<MatOfPoint>();
        yellowRect = new ArrayList<Rect>();
        YellowRect = new Rect();
        biggestContour = new MatOfPoint();

        this.telemetry = telemetry;
    }

    // Filters the contours to be greater than a specific area in order to be tracked
    public boolean filterContours(MatOfPoint contour) {
        return Imgproc.contourArea(contour) > 100;
    }

    // Yellow masking thresholding values:
    Scalar lowYellow = new Scalar(10, 125, 150); //10, 100, 50
    Scalar highYellow = new Scalar(35, 255, 255); //35, 255, 255

    // Mat object for the yellow mask
    Mat maskYellow = new Mat();

    // Mat object for HSV color space
    Mat HSV = new Mat();

    // Kernel size for blurring
    Size kSize = new Size(5, 5);
    Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2 * 2) + 1, (2 * 2) + 1));

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, HSV, Imgproc.COLOR_RGB2HSV);
        Imgproc.erode(HSV, HSV, kernel);

        if (YELLOW) {
            // Finds the pixels within the thresholds and puts them in the mat object "maskYellow"
            inRange(HSV, lowYellow, highYellow, maskYellow);

            // Clears the arraylists
            yellowContours.clear();
            yellowRect.clear();

            // Finds the contours and draws them on the screen
            Imgproc.findContours(maskYellow, yellowContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
            Imgproc.drawContours(input, yellowContours, -1, GOLD); //input

            // Iterates through each contour
            for (int i = 0; i < yellowContours.size(); i++) {

                // Filters out contours with an area less than 50 (defined in the filter contours method)
                if (filterContours(yellowContours.get(i))) {
                    // Creates a bounding rect around each contour and the draws it

                    biggestContour = Collections.max(yellowContours, (t0, t1) -> {
                        return Double.compare(Imgproc.boundingRect(t0).width, Imgproc.boundingRect(t1).width);
                    });

                    YellowRect = Imgproc.boundingRect(biggestContour);
                    yellowRect.add(Imgproc.boundingRect(yellowContours.get(i)));
                    Imgproc.rectangle(input, yellowRect.get(yellowContourCount), GOLD, 2);
                    // Creates a count for the amount of yellow contours on the the screen
                    yellowContourCount++;
                }
            }



            telemetry.addData("Yellow Contour Count", yellowContourCount);

            // Displays the position of the center of each bounding rect (rect.x/y returns the top left position)
            for (int i = 0; i < yellowRect.size(); i++){
                telemetry.addData("Yellow Contour " + (i+1), "%7d,%7d", yellowRect.get(i).x + (yellowRect.get(i).width/2), yellowRect.get(i).y + (yellowRect.get(i).height/2));
            }

            maskYellow.release();
        }

        yellowContourCount = 0;

        HSV.release();

        return input;
    }
}