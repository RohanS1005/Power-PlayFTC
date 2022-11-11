package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._TeleOp;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

@TeleOp(group="FinalTeleOp")
public class FinalTeleOp extends _TeleOp {

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.TeleOp1);
        Robot.setFieldSide(Robot.FieldSide.RED);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.TeleOp2);
        Robot.getIMU().willUpdate(true);
    }

    @Override
    public void loop() {
        Robot.update();
        telemetry.addLine("Drivetrain IMU: " + Robot.getIMU().getYaw());

        if(gamepad1.left_stick_x!=0 || gamepad1.left_stick_y!=0){
            double left_stick_y = -gamepad1.left_stick_y;
            double left_stick_x = gamepad1.left_stick_x;
            double joyStickAngle = (Math.toDegrees(Math.atan2(left_stick_y, left_stick_x)) + 360) % 360;
            double speed = Math.hypot(left_stick_x, left_stick_y)/2.5;
            Robot.getDrivetrain().runSpeedAngle(speed, joyStickAngle,0);
        }
        else if(gamepad1.right_stick_x!=0 || gamepad1.right_stick_y!=0){
            double right_stick_y = -gamepad1.right_stick_y;
            double right_stick_x = gamepad1.right_stick_x;
            double joyStickAngleCrawl = (Math.toDegrees(Math.atan2(right_stick_y, right_stick_x)) + 360) % 360;
            double speedCrawl = Math.hypot(right_stick_x, right_stick_y)/2.5;
            Robot.getDrivetrain().runSpeedAngle(speedCrawl, joyStickAngleCrawl ,0);
        }

        else if (gamepad1.left_stick_button) {
            Robot.getDrivetrain().runSpeed(0.5, _Drivetrain.Movements.ccw);
        }
        else if (gamepad1.right_stick_button) {
            Robot.getDrivetrain().runSpeed(0.5, _Drivetrain.Movements.cw);
        }
        else {
            Robot.getDrivetrain().stop();
        }

        //left trigger up and right trigger down
        if (gamepad1.left_trigger != 0) {
            Robot.getLinearslide().runSpeed(0.5);
        }
        else if (gamepad1.right_trigger != 0) {
            Robot.getLinearslide().runSpeed(-0.5);
        }
        else {
            Robot.getLinearslide().stop();
        }
    }
}