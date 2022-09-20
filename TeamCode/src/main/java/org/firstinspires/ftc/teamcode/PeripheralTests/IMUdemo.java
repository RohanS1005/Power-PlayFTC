package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="IMUDemo", group="PeripheralTest")
public class IMUdemo extends _Autonomous {

    private State _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init(){
        Robot.setup(hardwareMap,telemetry, Robot.SetupType.CraneIMU,Robot.SetupType.CraneLift,Robot.SetupType.Bucket, Robot.SetupType.CranePivot);
        Robot.setCraneLiftDegree(0);
        Robot.setCranePivotDegree(0);
        Robot.getBucket().setDegree(105);
    }

    @Override
    public void init_loop() {
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();
        Robot.getCranePivot().update();
        Robot.getCranePivotPID().update();
        Robot.getBucket().update();
    }

    @Override
    public void start() {
        _state = State.FIRST_MOTION;
        _justEntered = true;
    }

    @Override
    public void loop() {
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();
        Robot.getCranePivot().update();
        Robot.getCranePivotPID().update();
        Robot.getBucket().update();

        switch (_state) {
            case FIRST_MOTION:
                if (_justEntered) {
                    _justEntered = false;
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 3000;
                    Robot.setCraneLiftDegree(50);
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = State.SECOND_MOTION;
                    _justEntered = true;
                }
                else {
                    Robot.getBucket().setDegree(105+Robot.getCraneIMU().getRoll());
                    telemetry.addLine("Bucket: " + String.valueOf(Robot.getBucket().getDegree()));
                    telemetry.addLine("Lift: " + String.valueOf(Robot.getCraneIMU().getRoll()));
                    telemetry.addLine("Pivot: " + String.valueOf(Robot.getCraneIMU().getYaw()));
                }
                break;
            case SECOND_MOTION:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.setCranePivotDegree(90);
                    Robot.setCraneLiftDegree(-15);
                }
                Robot.getBucket().setDegree(105+Robot.getCraneIMU().getRoll());
                telemetry.addLine("Bucket: " + String.valueOf(Robot.getBucket().getDegree()));
                telemetry.addLine("Lift: " + String.valueOf(Robot.getCraneIMU().getRoll()));
                telemetry.addLine("Pivot: " + String.valueOf(Robot.getCraneIMU().getYaw()));
                break;
        }
    }

    private enum State {
        FIRST_MOTION,
        SECOND_MOTION
    }
}
