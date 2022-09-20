package org.firstinspires.ftc.teamcode.DriverTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._IMU;

@Autonomous(name="IMUTest", group="DriverTest")
public class IMUTest extends _Autonomous {

    private _IMU _imu;
    private States _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _imu = new _IMU("imu", false, true);
        _justEntered = true;
        _state = States.UPDATE_TIME;
    }

    @Override
    public void init_loop() {
        _imu.update();
        telemetry.addLine(_imu.getName());
        telemetry.addLine(String.valueOf(_imu.getYaw()));
    }

    @Override
    public void start() {
        _imu.willUpdate(true);
    }

    @Override
    public void loop() {
        _imu.update();
        telemetry.addLine(_imu.getName());
        telemetry.addLine(String.valueOf(_imu.getYaw()));
        telemetry.addLine(_state.name());

        switch (_state) {
            case UPDATE_TIME:
                if (_justEntered) {
                    _justEntered = false;
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 5000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = States.NO_UPDATE_TIME;
                    _justEntered = true;
                }
                break;
            case NO_UPDATE_TIME:
                if (_justEntered) {
                    _justEntered = false;
                    _imu.willUpdate(false);
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 3000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = States.UPDATE_UNLIMITED;
                    _justEntered = true;
                }
                break;
            case UPDATE_UNLIMITED:
                if (_justEntered) {
                    _justEntered = false;
                    _imu.willUpdate(true);
                }
                break;
        }
    }

    private enum States {
        UPDATE_TIME,
        NO_UPDATE_TIME,
        UPDATE_UNLIMITED
    }
}