package org.firstinspires.ftc.teamcode.DriverTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Auton.RedLeft;
import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Color;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

@Autonomous(name="ColorTest", group="DriverTest")
public class ColorTest extends _Autonomous {

    private _Color _color;
    private States _state;
    private boolean _justEntered;
    private double _startTime;
    private double _elapsedTime;
    private String parkingSpot;

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
        telemetry.addLine(String.valueOf(_color.getName()));
        telemetry.addLine(String.valueOf(_color.getRed()));
        telemetry.addLine(String.valueOf(_color.getGreen()));
        telemetry.addLine(String.valueOf(_color.getBlue()));
        int _RED = _color.getRed();
        int _GREEN =_color.getGreen();
        int _BLUE = _color.getBlue();

        int[] colors = {_RED, _GREEN, _BLUE};

        // find the max color
        int maxColor = 0;
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] > maxColor) maxColor = colors[i];
        }

        if (maxColor == _RED) {
            telemetry.addLine("Red");
        } else if (maxColor == _GREEN) {
            telemetry.addLine("Green");
        } else {
            telemetry.addLine("Blue");
        }
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
                    _state = States.readcolor;
                }
                break;
            case readcolor:
                if (_justEntered) {
                    _justEntered = false;
                    int _RED = _color.getRed();
                    int _GREEN =_color.getGreen();
                    int _BLUE = _color.getBlue();

                    int[] colors = {_RED, _GREEN, _BLUE};

                    // find the max color
                    int maxColor = 0;
                    for (int i = 0; i < colors.length; i++) {
                        if (colors[i] > maxColor) maxColor = colors[i];
                    }

                    if (maxColor == _RED) {
                        telemetry.addLine("Red");
                    } else if (maxColor == _GREEN) {
                        telemetry.addLine("Green");
                    } else {
                        telemetry.addLine("Blue");
                    }
                    Robot.getLinearslide().runTime(0, 10000);

                }
                else  {
                    _justEntered = true;
                    _state = States.park;
                }
                break;
            case park:
                if (_justEntered){
                    _justEntered=false;
                    if (parkingSpot.equals("red")){
                        Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.left);
                    }
                    else if(parkingSpot.equals("green")){
                        Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.right);
                    }
                    else {
                        Robot.getDrivetrain().runDistance(0.5, 1, _Drivetrain.Movements.forward);
                    }
                }
            else {
                _justEntered = true;
                }
        }
    }

    private enum States {
        UPDATE_TIME,
        NO_UPDATE_TIME,
        UPDATE_UNLIMITED,
        readcolor,
        park;
    }
}