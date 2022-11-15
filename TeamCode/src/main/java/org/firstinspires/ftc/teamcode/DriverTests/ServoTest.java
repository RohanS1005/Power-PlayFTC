package org.firstinspires.ftc.teamcode.DriverTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Servo;

@Autonomous(name="ServoTest", group="DriverTest")
public class ServoTest extends _Autonomous {

    private _Servo _claw;
    private _Servo _claw6;
    private States _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _claw = new _Servo("claw",Servo.Direction.REVERSE, 0, 0.5, 0,0, 0, 0.5, 90);
        _claw.setDegree(0);
        _justEntered = true;
        _state = States.SET_POS;
    }

    @Override
    public void loop(){
        _claw.update();
        telemetry.addLine(String.valueOf(_claw.getName()));
        telemetry.addLine(String.valueOf(_claw.getPosition()));
        telemetry.addLine(String.valueOf(_claw.getDegree()));
        telemetry.addLine(String.valueOf(_claw.isBusy()));
        telemetry.addLine(_state.name());

        switch(_state){
            case SET_POS:
                _claw.setPosition(1);
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
                _claw.setDegree(90);
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
                    _claw.setSlowPosition(0.85, 2000);
                }
                else if (!_claw.isBusy()) {
                    _state = States.SET_SLOW_DEG;
                    _justEntered = true;
                }
                break;
            case SET_SLOW_DEG:
                if (_justEntered) {
                    _justEntered = false;
                    _claw.setSlowDegree(90, 2000);
                }
                else if (!_claw.isBusy()) {
                    _state = States.SET_SLOW_DEG_INTERRUPTED;
                    _justEntered = true;
                }
                break;
            case SET_SLOW_DEG_INTERRUPTED:
                if (_justEntered) {
                    _justEntered = false;
                    _claw.setSlowDegree(270, 4000);
                }
                else if (_claw.getDegree() >= 225) {
                    _state = States.STOP;
                    _justEntered = true;
                }
                break;
            case STOP:
                if (_justEntered) {
                    _justEntered = false;
                    _claw.resetForNextRun();
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
