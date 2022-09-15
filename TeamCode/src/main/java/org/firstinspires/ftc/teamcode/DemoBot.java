package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoControllerEx;

public class DemoBot extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        Servo claw1, claw2;

        claw1 = hardwareMap.servo.get("claw1");
        ServoControllerEx claw1Controller = (ServoControllerEx) claw1.getController();
        int claw1ServoPort = claw1.getPortNumber();
        PwmControl.PwmRange claw1PwmRange = new PwmControl.PwmRange(500, 2500);
        claw1Controller.setServoPwmRange(claw1ServoPort, claw1PwmRange);

        claw2 = hardwareMap.servo.get("claw2");
        ServoControllerEx claw2Controller = (ServoControllerEx) claw2.getController();
        int claw2ServoPort = claw1.getPortNumber();
        PwmControl.PwmRange claw2PwmRange = new PwmControl.PwmRange(500, 2500);
        claw2Controller.setServoPwmRange(claw2ServoPort, claw2PwmRange);


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);


        }

    }
}