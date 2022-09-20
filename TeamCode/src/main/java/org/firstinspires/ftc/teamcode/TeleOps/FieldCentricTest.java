package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._TeleOp;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;


@TeleOp(name = "FieldCentricTest", group = "drive")
public class FieldCentricTest extends _TeleOp {
    @Override
    public void init(){
        telemetry.setAutoClear(true);
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.Drivetrain,Robot.SetupType.IMU);
    }

    @Override
    public void start() {
        Robot.getIMU().willUpdate(true);
    }

    @Override
    public void loop() {
        telemetry.update();
        Robot.getIMU().update();
        Robot.getDrivetrain().update();
        double right_stick_y = -gamepad1.right_stick_y;
        double right_stick_x = gamepad1.right_stick_x;
        double atanResult = Math.atan2(right_stick_y, right_stick_x);
        double toDegrees = Math.toDegrees(atanResult);
        double joyStickAngle = (toDegrees + 360) % 360;
        double posAng = Robot.getIMU().getYaw();
        double speed = Math.hypot(right_stick_x, right_stick_y);
        telemetry.addLine(String.valueOf(right_stick_x));
        telemetry.addLine(String.valueOf(right_stick_y));
        telemetry.addLine(String.valueOf(atanResult));
        telemetry.addLine(String.valueOf(toDegrees));
        telemetry.addLine(String.valueOf(joyStickAngle));
        telemetry.addLine(String.valueOf(posAng));
        telemetry.addLine(String.valueOf(speed));
        telemetry.addLine(String.valueOf(-posAng + (joyStickAngle - 90)));

        if(gamepad1.right_stick_x!=0 || gamepad1.right_stick_y!=0){

            Robot.getDrivetrain().runSpeedAngle(speed,-posAng + (joyStickAngle - 90),0);
        }
//        else if(gamepad1.left_bumper && (gamepad1.right_stick_x!=0 || gamepad1.right_stick_y!=0)){
//            Robot.getDrivetrain().runSpeedAngle(speed,-posAng + (joyStickAngle - 90),0, _Drivetrain.Movements.ccw);
//        }
//        else if(gamepad1.right_bumper && (gamepad1.right_stick_x!=0 || gamepad1.right_stick_y!=0)){
//            Robot.getDrivetrain().runSpeedAngle(speed,-posAng + (joyStickAngle - 90),0, _Drivetrain.Movements.cw);
//        }
        else if(gamepad1.left_bumper){
            Robot.getDrivetrain().runSpeed(0.8, _Drivetrain.Movements.ccw);
        }
        else if(gamepad1.right_bumper){
            Robot.getDrivetrain().runSpeed(0.8, _Drivetrain.Movements.cw);
        }
        else{
            Robot.getDrivetrain().stop();
        }
    }
}