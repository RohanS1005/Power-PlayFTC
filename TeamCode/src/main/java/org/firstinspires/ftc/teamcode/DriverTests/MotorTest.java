package org.firstinspires.ftc.teamcode.DriverTests;

import static org.firstinspires.ftc.teamcode.Control.Robot.MM_PER_INCH;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Motor;

@Autonomous(name="MotorTest", group="DriverTest")
public class MotorTest extends _Autonomous {

    private _Motor _FR;
    private States _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _FR = new _Motor("motorFR", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, 96/MM_PER_INCH, true);
        _FR.setTypicalSpeed(0.5);
        _state = States.RUN_DIST;
        _justEntered = true;
    }

    @Override
    public void loop() {
        _FR.update();
        telemetry.addLine(String.valueOf(_FR.getName()));
        telemetry.addLine(String.valueOf(_FR.getWheelDiameterInches()));
        telemetry.addLine(String.valueOf(_FR.getCountsPerInch()));
        telemetry.addLine(String.valueOf(_FR.getCountsPerDegree()));
        telemetry.addLine(String.valueOf(_FR.getSpeed()));
        telemetry.addLine(String.valueOf(_FR.getCounts()));
        telemetry.addLine(String.valueOf(_FR.isBusy()));
        telemetry.addLine(_state.name());

        switch (_state) {
            case RUN_DIST:
                if (_justEntered) {
                    _justEntered = false;
                    _FR.runDistance(12);
                }
                else if (!_FR.isBusy()) {
                    _state = States.RUN_TIME;
                    _justEntered = true;
                }
                break;
            case RUN_TIME:
                if (_justEntered) {
                    _justEntered = false;
                    _FR.runTime(-1, 3000);
                }
                else if (!_FR.isBusy()) {
                    _state = States.RUN_INTERRUPTED;
                    _justEntered = true;
                }
                break;
            case RUN_INTERRUPTED:
                if (_justEntered) {
                    _justEntered = false;
                    _FR.runTime(5000);
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 3000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _FR.resetForNextRun();
                    _state = States.SWITCH_MODE;
                    _justEntered = true;
                }
                break;
            case SWITCH_MODE:
                _FR = new _Motor("motorFR", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                        DcMotor.ZeroPowerBehavior.BRAKE, true);
                _FR.setTypicalSpeed(0.5);
                _state = States.RUN_DEGREES;
                break;
            case RUN_DEGREES:
                if (_justEntered) {
                    _justEntered = false;
                    _FR.runDegrees(-0.25, -1080);
                }
                else if (!_FR.isBusy()) {
                    _state = States.RUN_ROTATIONS;
                    _justEntered = true;
                }
                break;
            case RUN_ROTATIONS:
                if (_justEntered) {
                    _justEntered = false;
                    _FR.runRotations(10);
                }
                else if (!_FR.isBusy()) {
                    _state = States.RUN_SPEED;
                    _justEntered = true;
                }
                break;
            case RUN_SPEED:
                if (_justEntered) {
                    _justEntered = false;
                    _FR.runSpeed(-0.5);
                }
                break;
        }
    }

    private enum States {
        RUN_DIST,
        RUN_TIME,
        RUN_INTERRUPTED,
        SWITCH_MODE,
        RUN_DEGREES,
        RUN_ROTATIONS,
        RUN_SPEED
    }
}