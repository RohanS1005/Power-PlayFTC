package org.firstinspires.ftc.teamcode.Drivers;


import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Control.Robot;

public class _Color {

    private final String _NAME;
    private final double _ELAPSED_TIME;

    private final ColorSensor _color;
    private double _lastUpdateTime = 0;
    private boolean _willUpdate;
    private boolean _justStartedUpdating;

    private int _RED;
    private int _GREEN;
    private int _BLUE;

    public _Color(String name, double elapsedTime, boolean willUpdate) {
        _NAME = name;
        _ELAPSED_TIME = elapsedTime;
        _color = Robot.hardwareMap.get(ColorSensor.class, _NAME);
    }


    public void willUpdate(boolean willUpdate) {
        _willUpdate = willUpdate;
        _justStartedUpdating = _willUpdate;
        _lastUpdateTime = Robot.runtime.milliseconds() - _ELAPSED_TIME;
    }

    public void update() {
        if (_willUpdate && (Robot.runtime.milliseconds() >= _lastUpdateTime + _ELAPSED_TIME)) {
            // fill this code
            _RED = _color.red();
            _GREEN = _color.green();
            _BLUE = _color.blue();

            int[] colors = {_RED, _GREEN, _BLUE};

            // find the max color
            int maxColor = 0;
            for (int i = 0; i < colors.length; i++) {
                if (colors[i] > maxColor) maxColor = colors[i];
            }

            if (maxColor == _RED) {
                // park in spot 1
            } else if (maxColor == _GREEN) {
                // park in spot 2
            } else {
                // park in spot 3
            }
        }
    }

    public String getName() {
        return _NAME;
    }

    public int getRed(){
        return _RED;
    }
    public int getGreen(){
        return _GREEN;
    }
    public int getBlue(){
        return _BLUE;
    }
}