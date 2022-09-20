package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._TeleOp;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

@TeleOp(group = "drive")
public class TeleOpCollectionTest extends _TeleOp {

    @Override
    public void init(){
        telemetry.setAutoClear(true);
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.Drivetrain,Robot.SetupType.IMU,Robot.SetupType.CraneIMU,Robot.SetupType.CraneLift,Robot.SetupType.Bucket,Robot.SetupType.CranePivot,Robot.SetupType.Intake);
        Robot.setCraneLiftDegree(-65.5);
        Robot.getBucket().setDegree(130);
        Robot.setCranePivotDegree(0);

    }

    public void init_loop(){
        telemetry.update();
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();
        Robot.getCranePivot().update();
        Robot.getCranePivotPID().update();
        Robot.getBucket().update();
        telemetry.addLine("Lift: "+String.valueOf(Robot.getCraneIMU().getRoll()));
        telemetry.addLine("Bucket: "+String.valueOf(Robot.getBucket().getDegree()));
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
        Robot.getIntake().update();
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();
        Robot.getCranePivot().update();
        Robot.getCranePivotPID().update();
        Robot.getBucket().update();

        double right_stick_y = -gamepad1.right_stick_y;
        double right_stick_x = gamepad1.right_stick_x;
        double atanResultCrawl = Math.atan2(right_stick_y, right_stick_x);
        double toDegreesCrawl = Math.toDegrees(atanResultCrawl);
        double joyStickAngleCrawl = (toDegreesCrawl + 360) % 360;
        double posAngCrawl = Robot.getIMU().getYaw();
        double speedCrawl = Math.hypot(right_stick_x, right_stick_y)/2.5;

        double left_stick_y = -gamepad1.left_stick_y;
        double left_stick_x = gamepad1.left_stick_x;
        double atanResult = Math.atan2(left_stick_y, left_stick_x);
        double toDegrees = Math.toDegrees(atanResult);
        double joyStickAngle = (toDegrees + 360) % 360;
        double posAng = Robot.getIMU().getYaw();
        double speed = Math.hypot(left_stick_x, left_stick_y);

        if(gamepad1.left_stick_x!=0 || gamepad1.left_stick_y!=0){
            Robot.getDrivetrain().runSpeedAngle(speed,-posAng + (joyStickAngle - 90),0);
        }
        else if(gamepad1.right_stick_x!=0 || gamepad1.right_stick_y!=0){
            Robot.getDrivetrain().runSpeedAngle(speedCrawl,-posAngCrawl + (joyStickAngleCrawl - 90),0);
        }
        else if(gamepad1.left_stick_button){
            Robot.getDrivetrain().runSpeed(0.8, _Drivetrain.Movements.ccw);
        }
        else if(gamepad1.right_stick_button){
            Robot.getDrivetrain().runSpeed(0.8, _Drivetrain.Movements.cw);
        }
        else if(gamepad1.left_bumper){
            Robot.getIntake().runSpeed(0.8);
        }
        else if(gamepad1.right_bumper){
            Robot.getIntake().runSpeed(-0.8);
        }
        else if(gamepad1.a){
            Robot.getIntake().setTypicalSpeed(-0.3);
            Robot.getIntake().runTime(3000);
            Robot.getBucket().setSlowDegree(40,10);
            telemetry.addLine("I got here");
        }
        else if(gamepad1.y){
            Robot.setCraneLiftDegree(0);
        }
        else if(gamepad1.x){
            Robot.getIntake().runSpeed(.3);
            Robot.setCraneLiftDegree(-65.5);
            if(Robot.getCraneIMU().getRoll()<-62 && Robot.getCraneIMU().getRoll()>-68){
                Robot.getBucket().setDegree(130);
            }
        }
        else if(gamepad1.dpad_down){ //rohan help
            Robot.setCranePivotDegree(Robot.getCraneIMU().getYaw()+2);
        }
        else if(gamepad1.dpad_up){
            Robot.setCranePivotDegree(Robot.getCraneIMU().getYaw()-2);
        }
        //3 drop
        else if(gamepad2.b){
            Robot.getBucket().setDegree(180+46+35);
            Robot.setCraneLiftDegree(46);
        }
        //3 hold
        else if (gamepad2.dpad_up) {

            Robot.getBucket().setDegree(180-22);
            Robot.setCraneLiftDegree(46);
        }
        //2 drop
        else if (gamepad2.dpad_right) {
            Robot.getBucket().setDegree(220);
            Robot.setCraneLiftDegree(10);
        }
        //2 hold
        else if (gamepad2.dpad_down) {
            Robot.getBucket().setDegree(180-55);
            Robot.setCraneLiftDegree(-30);
        }
        //1 drop
        else if (gamepad2.dpad_left) {
            Robot.getBucket().setDegree(180);
            Robot.setCraneLiftDegree(-30);
        }
        //1 hold
        else if (gamepad2.a) {
            Robot.getBucket().setDegree(98);
            Robot.setCraneLiftDegree(-60);
        }
        //alliance hold
        else if (gamepad2.x) {
            Robot.getBucket().setDegree(125);
            Robot.setCraneLiftDegree(-25);
        }
        //2 drop
        else if (gamepad2.y) {
            Robot.getBucket().setDegree(175);
            Robot.setCraneLiftDegree(-25);
        }
        else{
            Robot.getDrivetrain().stop();
        }
    }
}