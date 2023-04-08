package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class GuiTest extends LinearOpMode {
    String poles = "";
    int strlength = 0;
    @Override
    public void runOpMode() {
        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("Select poles", "");
            telemetry.addData("Poles selected", poles);
            if (gamepad1.a) {
                strlength = poles.length();
                poles += "FAR_HIGH_POLE ";
            } else if (gamepad1.b) {
                strlength = poles.length();
                poles += "HIGH_POLE ";
            } else if (gamepad1.y) {
                strlength = poles.length();
                poles += "MID_POLE ";
            } else if (gamepad1.x) {
                poles = poles.substring(0, strlength);
            }
            telemetry.update();
        }
    }
}
