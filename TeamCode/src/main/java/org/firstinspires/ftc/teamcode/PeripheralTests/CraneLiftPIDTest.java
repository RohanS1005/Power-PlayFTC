package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="CraneLiftPIDTest", group="PeripheralTest")
public class CraneLiftPIDTest extends _Autonomous {

    private State _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.CraneIMU, Robot.SetupType.CraneLift);
        Robot.setCraneLiftDegree(-45);
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
                    _elapsedTime = 2000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = State.LOWER_CRANE;
                    _justEntered = true;
                }
                else {
                    telemetry.addLine("Roll: " + Robot.getCraneIMU().getRoll());
                }
                break;
            case LOWER_CRANE:
                Robot.getCraneLift().update();
                Robot.getCraneLiftPID().update();

                telemetry.addLine("Roll: " + Robot.getCraneIMU().getRoll());
                telemetry.addLine("Input: " + Robot.getCraneLiftPID().getLatestInputVal());
                telemetry.addLine("Output: " + Robot.getCraneLiftPID().getLatestOutputVal());
                break;
        }
    }

    @Override
    public void start() {
        Robot.setCraneLiftDegree(45);
    }

    @Override
    public void loop() {
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();

        telemetry.addLine("Roll: " + Robot.getCraneIMU().getRoll());
        telemetry.addLine("Input: " + Robot.getCraneLiftPID().getLatestInputVal());
        telemetry.addLine("Output: " + Robot.getCraneLiftPID().getLatestOutputVal());
    }

    private enum State {
        WAIT,
        LOWER_CRANE
    }
}