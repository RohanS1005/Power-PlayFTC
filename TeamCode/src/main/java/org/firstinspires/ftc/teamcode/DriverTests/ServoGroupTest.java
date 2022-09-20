package org.firstinspires.ftc.teamcode.DriverTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Servo;
import org.firstinspires.ftc.teamcode.Drivers._ServoGroup;

@Autonomous(name="ServoGroupTest", group="DriverTest")
public class ServoGroupTest extends _Autonomous {

    private _ServoGroup _bucket;
    private States _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _Servo left = new _Servo("bucketLeft", Servo.Direction.FORWARD, 0, 1, 1,
                0.17, 90, 0.51, 180);
        _Servo right = new _Servo("bucketRight", Servo.Direction.REVERSE, 0, 1, 1,
                0.17, 90, 0.51, 180);
        _bucket = new _ServoGroup(left, right);
        _state = States.SET_DEG;
        _justEntered = true;
    }

    @Override
    public void loop() {
        _bucket.update();
        telemetry.addLine(String.valueOf(_bucket.getServoNum()));
        telemetry.addLine(String.valueOf(_bucket.getPosition()));
        telemetry.addLine(String.valueOf(_bucket.getDegree()));
        telemetry.addLine(String.valueOf(_bucket.isBusy()));
        telemetry.addLine(_state.name());

        switch(_state) {
            case SET_DEG:
                _bucket.setDegree(45);
                _state = States.WAIT_1;
                break;
            case WAIT_1:
                if (_justEntered) {
                    _justEntered = false;
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 2000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = States.SET_POS;
                    _justEntered = true;
                }
                break;
            case SET_POS:
                _bucket.setPosition(0.17);
                _state = States.WAIT_2;
                break;
            case WAIT_2:
                if (_justEntered) {
                    _justEntered = false;
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 2000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = States.SET_SLOW_DEG;
                    _justEntered = true;
                }
                break;
            case SET_SLOW_DEG:
                if (_justEntered) {
                    _justEntered = false;
                    _bucket.setSlowDegree(270, 2000);
                }
                else if (!_bucket.isBusy()) {
                    _state = States.SET_SLOW_POS;
                    _justEntered = true;
                }
                break;
            case SET_SLOW_POS:
                if (_justEntered) {
                    _justEntered = false;
                    _bucket.setSlowPosition(0.17, 2000);
                }
                else if (!_bucket.isBusy()) {
                    _state = States.SET_SLOW_POS_INTERRUPTED;
                    _justEntered = true;
                }
                break;
            case SET_SLOW_POS_INTERRUPTED:
                if (_justEntered) {
                    _justEntered = false;
                    _bucket.setSlowPosition(0.85, 4000);
                }
                else if (_bucket.getDegree() >= 225) {
                    _state = States.STOP;
                    _justEntered = true;
                }
                break;
            case STOP:
                if (_justEntered) {
                    _justEntered = false;
                    _bucket.resetForNextRun();
                }
                break;
        }
    }

    private enum States {
        SET_DEG,
        WAIT_1,
        SET_POS,
        WAIT_2,
        SET_SLOW_DEG,
        SET_SLOW_POS,
        SET_SLOW_POS_INTERRUPTED,
        STOP
    }
}