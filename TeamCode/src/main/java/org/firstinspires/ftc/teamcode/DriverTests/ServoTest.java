package org.firstinspires.ftc.teamcode.DriverTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Servo;

@Autonomous(name="ServoTest", group="DriverTest")
public class ServoTest extends _Autonomous {

    private _Servo _right;
    private States _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _right = new _Servo("bucketRight",Servo.Direction.REVERSE, 0, 1, 0,0.17, 90, 0.51, 180);
        _right.setDegree(0);
        _justEntered = true;
        _state = States.SET_POS;
    }

    @Override
    public void loop(){
        _right.update();
        telemetry.addLine(String.valueOf(_right.getName()));
        telemetry.addLine(String.valueOf(_right.getPosition()));
        telemetry.addLine(String.valueOf(_right.getDegree()));
        telemetry.addLine(String.valueOf(_right.isBusy()));
        telemetry.addLine(_state.name());

        switch(_state){
            case SET_POS:
                _right.setPosition(1);
                _state = States.WAIT_1;
                break;
            case WAIT_1:
                if (_justEntered) {
                    _justEntered = false;
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 2000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = States.SET_DEG;
                    _justEntered = true;
                }
                break;
            case SET_DEG:
                _right.setDegree(90);
                _state = States.WAIT_2;
                break;
            case WAIT_2:
                if (_justEntered) {
                    _justEntered = false;
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 2000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = States.SET_SLOW_POS;
                    _justEntered = true;
                }
                break;
            case SET_SLOW_POS:
                if (_justEntered) {
                    _justEntered = false;
                    _right.setSlowPosition(0.85, 2000);
                }
                else if (!_right.isBusy()) {
                    _state = States.SET_SLOW_DEG;
                    _justEntered = true;
                }
                break;
            case SET_SLOW_DEG:
                if (_justEntered) {
                    _justEntered = false;
                    _right.setSlowDegree(90, 2000);
                }
                else if (!_right.isBusy()) {
                    _state = States.SET_SLOW_DEG_INTERRUPTED;
                    _justEntered = true;
                }
                break;
            case SET_SLOW_DEG_INTERRUPTED:
                if (_justEntered) {
                    _justEntered = false;
                    _right.setSlowDegree(270, 4000);
                }
                else if (_right.getDegree() >= 225) {
                    _state = States.STOP;
                    _justEntered = true;
                }
                break;
            case STOP:
                if (_justEntered) {
                    _justEntered = false;
                    _right.resetForNextRun();
                }
                break;
        }
    }

    private enum States {
        SET_POS,
        WAIT_1,
        SET_DEG,
        WAIT_2,
        SET_SLOW_POS,
        SET_SLOW_DEG,
        SET_SLOW_DEG_INTERRUPTED,
        STOP
    }
}
