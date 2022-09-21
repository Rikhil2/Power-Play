package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoControllerEx;

@Autonomous
public class demoAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("fl");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("bl");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("fr");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("br");

        Servo claw2;
        claw2 = hardwareMap.servo.get("claw2");
        ServoControllerEx claw2Controller = (ServoControllerEx) claw2.getController();
        int claw2ServoPort = claw2.getPortNumber();
        PwmControl.PwmRange claw2PwmRange = new PwmControl.PwmRange(600, 2208);
        claw2Controller.setServoPwmRange(claw2ServoPort, claw2PwmRange);

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        claw2.setPosition(0.58);
/*
        motorFrontLeft.setPower(1);
        motorBackLeft.setPower(1);
        motorFrontRight.setPower(1);
        motorBackRight.setPower(1);

        sleep(1000);
 */

    }
}
