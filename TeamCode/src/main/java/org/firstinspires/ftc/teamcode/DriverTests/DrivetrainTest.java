package org.firstinspires.ftc.teamcode.DriverTests;

import static org.firstinspires.ftc.teamcode.Control.Robot.MM_PER_INCH;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;
import org.firstinspires.ftc.teamcode.Drivers._Motor;

@Autonomous(name="DrivetrainTest", group="DriverTest")
public class DrivetrainTest extends _Autonomous {

    private _Drivetrain _drivetrain;
    private States _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        double wheelDiameter = 96/MM_PER_INCH;
        _Motor fr = new _Motor("motorFR", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _Motor fl = new _Motor("motorFL", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _Motor br = new _Motor("motorBR", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _Motor bl = new _Motor("motorBL", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _drivetrain = new _Drivetrain(fr, fl, br, bl, 1.0);
        _drivetrain.setTypicalSpeed(0.5);
        _state = States.RUN_DIST;
        _justEntered = true;
    }

    @Override
    public void loop() {
        _drivetrain.update();
        telemetry.addLine(String.valueOf(_drivetrain.getMotorNum()));
        telemetry.addLine(String.valueOf(_drivetrain.getSpeed()));
        telemetry.addLine(String.valueOf(_drivetrain.isBusy()));
        telemetry.addLine(_state.name());

        switch (_state) {
            case RUN_DIST:
                if (_justEntered) {
                    _justEntered = false;
                    _drivetrain.runDistance(-12, _Drivetrain.Movements.forward);
                }
                else if (!_drivetrain.isBusy()) {
                    _state = States.RUN_TIME;
                    _justEntered = true;
                }
                break;
            case RUN_TIME:
                if (_justEntered) {
                    _justEntered = false;
                    _drivetrain.runTime(0.55, 2000, _Drivetrain.Movements.forward);
                }
                else if (!_drivetrain.isBusy()) {
                    _state = States.RUN_INTERRUPTED;
                    _justEntered = true;
                }
                break;
            case RUN_INTERRUPTED:
                if (_justEntered) {
                    _justEntered = false;
                    _drivetrain.runTime(5000, _Drivetrain.Movements.left);
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 1000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _drivetrain.resetForNextRun();
                    _state = States.RUN_45_INTERRUPTED;
                    _justEntered = true;
                }
                break;
            case RUN_45_INTERRUPTED:
                if (_justEntered) {
                    _justEntered = false;
                    _drivetrain.runSpeedAngle(0.25, 45, 0);
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 5000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _drivetrain.resetForNextRun();
                    _state = States.RUN_135_INTERRUPTED;
                    _justEntered = true;
                }
                break;
            case RUN_135_INTERRUPTED:
                if (_justEntered) {
                    _justEntered = false;
                    _drivetrain.runSpeedAngle(-135, 0);
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 2000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _drivetrain.resetForNextRun();
                    _state = States.RUN_SPEED;
                    _justEntered = true;
                }
                break;
            case RUN_SPEED:
                if (_justEntered) {
                    _justEntered = false;
                    _drivetrain.runSpeed(_Drivetrain.Movements.cw);
                }
                break;
            case STOP:
                if (_justEntered) {
                    _justEntered = false;
                    _drivetrain.stop();
                }
                break;
        }
    }

    private enum States {
        RUN_DIST,
        RUN_TIME,
        RUN_INTERRUPTED,
        RUN_45_INTERRUPTED,
        RUN_135_INTERRUPTED,
        RUN_SPEED,
        STOP
    }
}