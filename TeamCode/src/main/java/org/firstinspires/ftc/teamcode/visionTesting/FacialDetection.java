package org.firstinspires.ftc.teamcode.visionTesting;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.openftc.easyopencv.OpenCvPipeline;

public class FacialDetection extends OpenCvPipeline {
    Telemetry telemetry;
    public FacialDetection(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    CascadeClassifier cascadeClassifier = new CascadeClassifier();

    @Override
    public Mat processFrame(Mat input) {
        MatOfRect facesDetected = new MatOfRect();
        int minFaceSize = Math.round(input.rows() * 0.1f);
        cascadeClassifier.load("/Users/rikhilkumar/StudioProjects/Power-Play-Rikhil/TeamCode/src/main/res/xml/haarcascade_frontalface_alt.xml");
        cascadeClassifier.detectMultiScale(input,
                facesDetected,
                1.1,
                3,
                Objdetect.CASCADE_SCALE_IMAGE,
                new Size(minFaceSize, minFaceSize),
                new Size()
        );
        Rect[] facesArray = facesDetected.toArray();
        for(Rect face : facesArray) {
            Imgproc.rectangle(input, face.tl(), face.br(), new Scalar(0, 0, 255), 3);
        }
        telemetry.addData("Faces Detected", facesArray.length);
        telemetry.update();
        return input;
    }
}