package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._TeleOp;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;
import org.firstinspires.ftc.teamcode.Drivers._PID;

@TeleOp(group="TeleOpRED")
public class FinalTeleOpRed extends _TeleOp {

    private boolean _movingBucket = false;
    private Robot.CranePreset _holdPreset = Robot.CRANE_TOP_LEVEL_HOLD;
    private Robot.CranePreset _dropPreset = Robot.CRANE_TOP_LEVEL_DROP;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.TeleOp1);
        Robot.setFieldSide(Robot.FieldSide.RED);
        Robot.setCraneLiftDegree(Robot.CRANE_COLLECTION_DROP.CRANE_LIFT_DEGREE);
        Robot.getBucket().setDegree(Robot.CRANE_COLLECTION_DROP.BUCKET_DEGREE);
    }

    @Override
    public void init_loop() {
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();
        Robot.getBucket().update();
        Robot.getCarousel().update();

        telemetry.addLine("Bucket: " + String.valueOf(Robot.getBucket().getDegree()));
        telemetry.addLine("Lift: " + String.valueOf(Robot.getCraneIMU().getRoll()));
        telemetry.update();
    }

    @Override
    public void start() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.TeleOp2);
        Robot.getIMU().willUpdate(true);
        Robot.setCranePreset(Robot.CRANE_COLLECTION_DROP);
    }

    @Override
    public void loop() {
        Robot.update();
        telemetry.addLine("Lift: " + Robot.getCraneIMU().getRoll());
        telemetry.addLine("Pivot: " + Robot.getCraneIMU().getYaw());
        telemetry.addLine("Bucket" + Robot.getBucket().getDegree());
        telemetry.addLine("Drivetrain IMU: " + Robot.getIMU().getYaw());

        if (gamepad1.a) {
            Robot.moveCraneToPreset(Robot.CRANE_TOP_LEVEL_HOLD, true);
            _holdPreset = Robot.CRANE_TOP_LEVEL_HOLD;
            _dropPreset = Robot.CRANE_TOP_LEVEL_DROP;
        }
        else if (gamepad1.b) {
            Robot.getBucket().setDegree(Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE);
            Robot.moveCraneToPreset(Robot.CRANE_COLLECTION_HOLD, false);
        }
        else if (gamepad1.x) {
            Robot.moveCraneToPreset(Robot.CRANE_SHARED_LEVEL_HOLD, true);
            _holdPreset = Robot.CRANE_SHARED_LEVEL_HOLD;
            _dropPreset = Robot.CRANE_SHARED_LEVEL_DROP;
        }
        else if (gamepad1.y) {
            Robot.getBucket().setDegree(Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE);
            Robot.moveCraneToPreset(Robot.CRANE_COLLECTION_HOLD, false);
        }

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

        if (gamepad1.left_bumper) {
            Robot.getIntake().runSpeed(0.8);
        }
        else if (gamepad1.right_bumper) {
            Robot.getIntake().runSpeed(-0.8);
        }
        else if (!_movingBucket) {
            Robot.getIntake().stop();
        }

        if (gamepad2.a
                && Robot.getBucket().getDegree() <= _holdPreset.BUCKET_DEGREE + Robot.ANGLE_RANGE
                && Robot.getBucket().getDegree() >= _holdPreset.BUCKET_DEGREE - Robot.ANGLE_RANGE) {
            if (_dropPreset == Robot.CRANE_CAPPING_DROP) {
                Robot.neglectBucketPosition();
                Robot.setCraneLiftDegree(_dropPreset.CRANE_LIFT_DEGREE);
            }
            else {
                Robot.getBucket().setSlowDegree(_dropPreset.BUCKET_DEGREE, 500);
            }
        }
        //carousel cw
        if(gamepad2.right_trigger!=0){
            Robot.getCarousel().runSpeed(0.3);
        }
        //carouselccw
        else if(gamepad2.left_trigger!=0){
            Robot.getCarousel().runSpeed(-0.3);
        }
        else{
            Robot.getCarousel().stop();
        }
        //pivot ccw
        if(gamepad1.dpad_right){

            Robot.getCranePivotPID().setState(_PID.State.MANUAL);
            Robot.getCraneIMU().resetYaw(-15);
            Robot.getCranePivot().runTime(.4,100);
            Robot.getCranePivotPID().setState(_PID.State.AUTOMATIC);
        }
        //pivot cw
        else if(gamepad1.dpad_left){
            Robot.getCranePivotPID().setState(_PID.State.MANUAL);
            Robot.getCraneIMU().resetYaw(15);
            Robot.getCranePivot().runTime(-.4,100);
            Robot.getCranePivotPID().setState(_PID.State.AUTOMATIC);
        }
        //arm lift up
        if(gamepad2.dpad_up){
            Robot.setCraneLiftDegree(Robot.getCraneIMU().getRoll()+10);
        }
        //arm lift down
        else if(gamepad2.dpad_down){
            Robot.setCraneLiftDegree(Robot.getCraneIMU().getRoll()-10);
        }
        //bucket up
        if(gamepad2.right_bumper){
            Robot.getBucket().setDegree(Robot.getBucket().getDegree()+1);
        }
        //bucket down
        else if(gamepad2.left_bumper){
            Robot.getBucket().setDegree(Robot.getBucket().getDegree()-1);
        }
        if(gamepad1.dpad_up
                && Robot.getCraneIMU().getRoll()<=Robot.CRANE_COLLECTION_DROP.CRANE_LIFT_DEGREE + Robot.ANGLE_RANGE
                && Robot.getCraneIMU().getRoll()>=Robot.CRANE_COLLECTION_DROP.CRANE_LIFT_DEGREE - Robot.ANGLE_RANGE
                && Robot.getBucket().getDegree()<= Robot.CRANE_COLLECTION_DROP.BUCKET_DEGREE + Robot.ANGLE_RANGE
                && Robot.getBucket().getDegree()>= Robot.CRANE_COLLECTION_DROP.BUCKET_DEGREE - Robot.ANGLE_RANGE) {
            Robot.getIntake().runTime(-0.3, 3000);
            Robot.getBucket().setSlowDegree(Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE,1000);
            _movingBucket = true;
        }
        else if(gamepad1.dpad_down
                && Robot.getCraneIMU().getRoll() <= Robot.CRANE_COLLECTION_HOLD.CRANE_LIFT_DEGREE + Robot.ANGLE_RANGE
                && Robot.getCraneIMU().getRoll() >= Robot.CRANE_COLLECTION_HOLD.CRANE_LIFT_DEGREE - Robot.ANGLE_RANGE
                && Robot.getBucket().getDegree() <= Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE + Robot.ANGLE_RANGE
                && Robot.getBucket().getDegree() >= Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE - Robot.ANGLE_RANGE) {
            //if bucket is outside go to intake
            Robot.getIntake().runTime(0.3, 3000);
            Robot.getBucket().setSlowDegree(Robot.CRANE_COLLECTION_DROP.BUCKET_DEGREE,1000);
            _movingBucket = true;
        }
        else if (_movingBucket && !Robot.getBucket().isBusy()) {
            _movingBucket = false;
        }

        if(gamepad2.y){
            Robot.moveCraneToPreset(Robot.CRANE_CAPPING_LIFT, true);
            _holdPreset = Robot.CRANE_CAPPING_LIFT;
            _dropPreset = Robot.CRANE_CAPPING_DROP;
        }
        else if(gamepad2.x){
            Robot.moveCraneToPreset(Robot.CRANE_CAPPING_COLLECT, false);
        }
        if(gamepad2.b){
            Robot.getIMU().resetYaw();
            Robot.getCraneIMU().resetYaw();
        }

        if(gamepad2.dpad_right){
            Robot.getCraneIMU().resetYaw(Robot.getCraneIMU().getYaw() - 2);
        }
        else if(gamepad2.dpad_left) {
            Robot.getCraneIMU().resetYaw(Robot.getCraneIMU().getYaw() + 2);
        }

        if (gamepad2.right_stick_button) {
            Robot.getCraneIMU().resetYaw(0);
        }
    }
}
