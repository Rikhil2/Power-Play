package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class MainPipeline extends OpenCvPipeline {
    public Scalar lower = new Scalar(66.6, 140.3, 0);
    public Scalar upper = new Scalar(225.3, 255, 92.1);

    int duckpos = 2;//left=0; middle=1; right=2;

    private Mat ycrcbMat       = new Mat();
    private Mat binaryMat      = new Mat();
    private Mat maskedInputMat = new Mat();


    Point A1 = new Point(0, 200);
    Point B1 = new Point(181, 288);

    Point A2 = new Point(181, 200);
    Point B2 = new Point(363, 288);

    Point A3 = new Point(363, 200);
    Point B3 = new Point(544, 288);

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2YCrCb);

        /*
         * This is where our thresholding actually happens.
         * Takes our "ycrcbMat" as input and outputs a "binary"
         * Mat to "binaryMat" of the same size as our input.
         * "Discards" all the pixels outside the bounds specified
         * by the scalars above (and modifiable with EOCV-Sim's
         * live variable tuner.)
         *
         * Binary meaning that we have either a 0 or 255 value
         * for every pixel.
         *
         * 0 represents our pixels that were outside the bounds
         * 255 represents our pixels that are inside the bounds
         */
        Core.inRange(ycrcbMat, lower, upper, binaryMat);

        /*
         * Release the reusable Mat so that old data doesn't
         * affect the next step in the current processing
         */
        maskedInputMat.release();

        /*
         * Now, with our binary Mat, we perform a "bitwise and"
         * to our input image, meaning that we will perform a mask
         * which will include the pixels from our input Mat which
         * are "255" in our binary Mat (meaning that they're inside
         * the range) and will discard any other pixel outside the
         * range (RGB 0, 0, 0. All discarded pixels will be black)
         */
        Core.bitwise_and(input, input, maskedInputMat, binaryMat);

        /*
         * The Mat returned from this method is the
         * one displayed on the viewport.
         *
         * To visualize our threshold, we'll return
         * the "masked input mat" which shows the
         * pixel from the input Mat that were inside
         * the threshold range.
         */

        Scalar GREEN = new Scalar(0, 255, 0);
        Scalar BLUE = new Scalar(0, 0, 255);
        Scalar RED = new Scalar(255, 0, 0);

        int avg_colorLeft = (int) Core.mean(maskedInputMat.submat(new Rect(A1, B1))).val[3];
        int avg_colorMiddle = (int) Core.mean(maskedInputMat.submat(new Rect(A2, B2))).val[3];
        int avg_colorRight = (int) Core.mean(maskedInputMat.submat(new Rect(A3, B3))).val[3];

        Imgproc.rectangle(
                maskedInputMat,
                A1,
                B1,
                RED,
                (int)0.5);
        Imgproc.rectangle(
                maskedInputMat,
                A2,
                B2,
                RED,
                (int)0.5);
        Imgproc.rectangle(
                maskedInputMat,
                A3,
                B3,
                RED,
                (int)0.5);

        int maximum = Math.max(Math.max(avg_colorLeft, avg_colorMiddle), avg_colorRight);

        if (maximum == avg_colorLeft) {
            duckpos = 0;
            Imgproc.rectangle(
                    maskedInputMat,
                    A1,
                    B1,
                    GREEN,
                    (int)0.5);
        }
        if (maximum == avg_colorMiddle) {
            duckpos = 1;
            Imgproc.rectangle(//test
                    maskedInputMat,
                    A2,
                    B2,
                    GREEN,
                    (int)0.5);
        }
        if (maximum == avg_colorRight) {
            duckpos = 2;
            Imgproc.rectangle(
                    maskedInputMat,
                    A3,
                    B3,
                    GREEN,
                    (int)0.5);
        }

        return maskedInputMat;
    }

    public int getDuckPosition()
    {
        return duckpos;
    }

}
