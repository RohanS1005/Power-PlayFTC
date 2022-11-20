package org.firstinspires.ftc.teamcode.Drivers;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Control.Robot;

public class _Color {

    private final String _NAME;

    private final ColorSensor _color;
    private int _red;
    private int _blue;
    private int _green;
    private double _hue;
    private double _sat;
    private double _val;

    public _Color(String name) {
        _NAME = name;
        _color = Robot.hardwareMap.get(ColorSensor.class, _NAME);
//        enableLed(true);
    }

    public void enableLed(boolean state) {
        _color.enableLed(state);
    }

    public void fetchData() {
        float hsv[] = {0F, 0F, 0F};
        _red = _color.red();
        _blue = _color.blue();
        _green = _color.green();
        Color.RGBToHSV(_red * 8, _green * 8, _blue * 8, hsv);
        _hue = hsv[0];
        _sat = hsv[1];
        _val = hsv[2];
    }

    public int getRed() {
        return _red;
    }

    public int getBlue() {
        return _blue;
    }

    public int getGreen() {
        return _green;
    }

    public double getHue() {
        return _hue;
    }

    public double getSat() {
        return _sat;
    }

    public double getVal() {
        return _val;
    }

    public String getName() {
        return _NAME;
    }
}