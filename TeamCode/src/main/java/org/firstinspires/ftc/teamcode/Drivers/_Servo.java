package org.firstinspires.ftc.teamcode.Drivers;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Control.Robot;

public class _Servo {

    private final String _NAME;
    private final double _MAX;
    private final double _MIN;
    private final double _POSITION_1;
    private final double _POSITION_2;
    private final double _ANGLE_1;
    private final double _ANGLE_2;
    private final double _POSITION_PER_DEGREE;
    private final double _0_DEGREE_POSITION;
    private final double _INTERVAL_MS;

    private final Servo _servo;
    private double _position;
    private double _targetPosition;
    private double _incrementPosition;
    private int _direction;
    private double _lastUpdateTime;
    private boolean _isBusy;

    public _Servo(String name, Servo.Direction direction, double min, double max, double startDegree, double position_1, double angle_1, double position_2, double angle_2) {
        _NAME = name;
        _MAX = max;
        _MIN = min;
        _POSITION_1 = Math.max(position_1, position_2);
        _POSITION_2 = Math.min(position_1, position_2);
        _ANGLE_1 = Math.max(angle_1, angle_2);
        _ANGLE_2 = Math.min(angle_1, angle_2);
        if ((_ANGLE_1 - _ANGLE_2) != 0) {
            _POSITION_PER_DEGREE = (_POSITION_1 - _POSITION_2)/(_ANGLE_1 - _ANGLE_2);
        }
        else {
            _POSITION_PER_DEGREE = 0;
        }
        _0_DEGREE_POSITION = _POSITION_1 - _ANGLE_1 * _POSITION_PER_DEGREE;
        _INTERVAL_MS = 10;
        _servo = Robot.hardwareMap.servo.get(_NAME);
        _servo.setDirection(direction);
        _setDegree(startDegree);
    }

    public void update() {
        if (_isBusy) {
            if (_direction > 0 ? _position >= _targetPosition : _position <= _targetPosition) {
                _resetForNextRun();
            }
            else if (Robot.runtime.milliseconds() >= _lastUpdateTime + _INTERVAL_MS) {
                _setPosition(_position + _incrementPosition);
                _lastUpdateTime = Robot.runtime.milliseconds();

                if (_direction > 0 ? _position >= _targetPosition : _position <= _targetPosition) {
                    _resetForNextRun();
                }
            }
        }
    }

    public void resetForNextRun() {
        _isBusy = false;
    }

    public void setPosition(double position) {
        if (!_isBusy) {
            _setPosition(position);
        }
    }

    public void setDegree(double degree) {
        if (!_isBusy) {
            _setDegree(degree);
        }
    }

    public void setSlowPosition(double position, double durationMS) {
        if (!_isBusy && _position != position) {
            _isBusy = true;
            _targetPosition = _clampPosition(position);
            _direction = _targetPosition > _position ? 1 : -1;
            _incrementPosition = _direction * (Math.abs(_targetPosition - _position) / (durationMS / _INTERVAL_MS));
            _lastUpdateTime = Robot.runtime.milliseconds() - _INTERVAL_MS;
        }
    }

    public void setSlowDegree(double degree, double durationMS) {
        setSlowPosition(_degreeToPosition(degree), durationMS);
    }

    public String getName() {
        return _NAME;
    }

    public double getPosition() {
        return _position;
    }

    public double getDegree() {
        return (_position - _0_DEGREE_POSITION) /_POSITION_PER_DEGREE;
    }

    public boolean isBusy() {
        return _isBusy;
    }

    private void _resetForNextRun() {
        _isBusy = false;
        _setPosition(_targetPosition);
    }

    private void _setPosition(double position) {
        Robot.telemetry.addLine("Just set position");
        _position = _clampPosition(position);
        _servo.setPosition(_position + _MIN);
    }

    private double _clampPosition(double position) {
        return Math.max(Math.min(position, _MAX - _MIN), 0);
    }

    private void _setDegree(double degree) {
        _setPosition(_clampPosition(_degreeToPosition(degree)));
    }

    private double _degreeToPosition(double degree) {
        return degree * _POSITION_PER_DEGREE + _0_DEGREE_POSITION;
    }
}