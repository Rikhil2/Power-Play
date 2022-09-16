package org.firstinspires.ftc.teamcode.vision;

import org.opencv.core.Mat;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.calib3d.*;

public class depthMapPipeline extends OpenCvPipeline {
    double distance;
    final Mat previousCameraFrameTest = new Mat();
    Mat previousCameraFrame = new Mat();
    int iteration;
    long nativeObj;

    void StereoBM(long addr) { nativeObj = addr; }


    public static final int
            PREFILTER_NORMALIZED_RESPONSE = 0,
            PREFILTER_XSOBEL = 1,
            BASIC_PRESET = 0,
            FISH_EYE_PRESET = 1,
            NARROW_PRESET = 2;

    public void StereoBM()
    {

        nativeObj = StereoBM_0();

        return;
    }

    public void StereoBM(int preset, int ndisparities, int SADWindowSize)
    {

        nativeObj = StereoBM_1(preset, ndisparities, SADWindowSize);

        return;
    }

    public void StereoBM(int preset)
    {

        nativeObj = StereoBM_2(preset);

        return;
    }

    public  void compute(Mat left, Mat right, Mat disparity, int disptype)
    {

        compute_0(nativeObj, left.nativeObj, right.nativeObj, disparity.nativeObj, disptype);

        return;
    }

    public  void compute(Mat left, Mat right, Mat disparity)
    {

        compute_1(nativeObj, left.nativeObj, right.nativeObj, disparity.nativeObj);

        return;
    }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }

    private static native void compute_0(long nativeObj, long left_nativeObj, long right_nativeObj, long disparity_nativeObj, int disptype);

    private static native void compute_1(long nativeObj, long left_nativeObj, long right_nativeObj, long disparity_nativeObj);

    private static native long StereoBM_0();

    private static native long StereoBM_1(int preset, int ndisparities, int SADWindowSize);

    private static native long StereoBM_2(int preset);

    private static native void delete(long nativeObj);

    @Override
    public Mat processFrame(Mat input) {
        previousCameraFrame = input;
        iteration++;
        if (iteration%2 == 0) {
            compute(previousCameraFrame, input, input);
            return input;
        } else if (iteration%2 ==1) {
            return input;
        }
        return input;
    }
}
