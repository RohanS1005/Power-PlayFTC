package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="CranePivotPIDTest", group="PeripheralTest")
public class CranePivotPIDTest extends _Autonomous {

    private State _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.CraneIMU, Robot.SetupType.CraneLift, Robot.SetupType.CranePivot, Robot.SetupType.Bucket);
        Robot.getBucket().setDegree(135+13.4);
        Robot.setCraneLiftDegree(45);
        Robot.setCranePivotDegree(-90);
        _state = State.WAIT;
        _justEntered = true;
    }

    @Override
    public void init_loop() {
        Robot.getCraneIMU().update();

        switch (_state) {
            case WAIT:
                if (_justEntered) {
                    _justEntered = false;
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 3000;
                } else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = State.MOVE_CRANE;
                    _justEntered = true;
                } else {
                    telemetry.addLine("Roll: " + Robot.getCraneIMU().getRoll());
                    telemetry.addLine("Yaw: " + Robot.getCraneIMU().getYaw());
                }
                break;
            case MOVE_CRANE:
                Robot.getCraneLift().update();
                Robot.getCraneLiftPID().update();
                Robot.getCranePivot().update();
                Robot.getCranePivotPID().update();
                Robot.getBucket().update();

                telemetry.addLine("Roll: " + Robot.getCraneIMU().getRoll());
                telemetry.addLine("Yaw: " + Robot.getCraneIMU().getYaw());
                telemetry.addLine("Input: " + Robot.getCranePivotPID().getLatestInputVal());
                telemetry.addLine("Output: " + Robot.getCranePivotPID().getLatestOutputVal());
                break;
        }
    }

    @Override
    public void start() {
        Robot.getBucket().setSlowDegree(105+13.4, 12);
        Robot.setCraneLiftDegree(15);
        Robot.setCranePivotDegree(90);
    }

    @Override
    public void loop() {
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();
        Robot.getCranePivot().update();
        Robot.getCranePivotPID().update();
        Robot.getBucket().update();

        telemetry.addLine("Roll: " + Robot.getCraneIMU().getRoll());
        telemetry.addLine("Yaw: " + Robot.getCraneIMU().getYaw());
        telemetry.addLine("Input: " + Robot.getCranePivotPID().getLatestInputVal());
        telemetry.addLine("Output: " + Robot.getCranePivotPID().getLatestOutputVal());
    }

    private enum State {
        WAIT,
        MOVE_CRANE
    }
}