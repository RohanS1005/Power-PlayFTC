package org.firstinspires.ftc.teamcode.DriverTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Color;

@Autonomous(name="ColorTest", group="DriverTest")
public class ColorTest extends _Autonomous {

    private _Color _color;
    private States _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _color = new _Color("color", 100, true);
        _justEntered = true;
        _state = States.UPDATE_TIME;
    }

    @Override
    public void init_loop() {
        _color.update();
        telemetry.addLine(_color.getName());
        telemetry.addLine(String.valueOf(_color.getRed()));
        telemetry.addLine(String.valueOf(_color.getGreen()));
        telemetry.addLine(String.valueOf(_color.getBlue()));
    }

    @Override
    public void start() {
        _color.willUpdate(true);
    }

    @Override
    public void loop() {
        _color.update();
        telemetry.addLine(_color.getName());
        telemetry.addLine(String.valueOf(_color.getRed()));
        telemetry.addLine(String.valueOf(_color.getGreen()));
        telemetry.addLine(String.valueOf(_color.getBlue()));
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
                    _color.willUpdate(false);
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
                    _color.willUpdate(true);
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