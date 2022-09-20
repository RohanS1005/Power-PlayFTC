package org.firstinspires.ftc.teamcode.DriverTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._OpenCV;

@Autonomous(name="OpenCVTest", group="DriverTest")
public class OpenCVTest extends _Autonomous {

    private _OpenCV _webcam;
    private States _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _webcam = new _OpenCV("Webcam 1", 320, 240);
        _justEntered = true;
        _state = States.VIEWPORT_ON;
    }

    @Override
    public void init_loop() {
        telemetry.addLine(_webcam.getName());
        telemetry.addLine(_state.name());

        switch (_state) {
            case VIEWPORT_ON:
                if (_justEntered) {
                    _justEntered = false;
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 5000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = States.VIEWPORT_OFF;
                    _justEntered = true;
                }
                break;
            case VIEWPORT_OFF:
                if (_justEntered) {
                    _justEntered = false;
                    _webcam.pauseLiveView();
                    _startTime = Robot.runtime.milliseconds();
                    _elapsedTime = 3000;
                }
                else if (Robot.runtime.milliseconds() >= _startTime + _elapsedTime) {
                    _state = States.VIEWPORT_ON_UNLIMITED;
                    _justEntered = true;
                }
                break;
            case VIEWPORT_ON_UNLIMITED:
                if (_justEntered) {
                    _justEntered = false;
                    _webcam.resumeLiveView();
                }
                break;
        }
    }

    @Override
    public void loop() {}

    private enum States {
        VIEWPORT_ON,
        VIEWPORT_OFF,
        VIEWPORT_ON_UNLIMITED
    }
}