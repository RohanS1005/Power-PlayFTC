package org.firstinspires.ftc.teamcode.Drivers;

import org.firstinspires.ftc.teamcode.Control.Robot;

public class _PID {

    private final GetDouble _sourceInput;
    private final SetDouble _sourceOutput;
    private final GetDouble _sourceSetPoint;
    private final double _FORWARD_KP;
    private final double _FORWARD_KI;
    private final double _FORWARD_KD;
    private final double _BACKWARD_KP;
    private final double _BACKWARD_KI;
    private final double _BACKWARD_KD;

    private double _sourceInputVal;
    private double _sourceOutputVal;
    private double _lastTime;
    private double _outputSum;
    private double _lastInput;
    private double _kp;
    private double _ki;
    private double _kd;
    private double _sampleTimeMS = 1000;
    private double _outMin;
    private double _outMax;
    private State _state;
    private Direction _direction = Direction.DIRECT;
    private ProportionalMode _proportionalMode = ProportionalMode.ERROR;
    private boolean _initializedLastInput;

    public _PID(GetDouble input, SetDouble output, GetDouble setPoint,
                double f_kp, double f_ki, double f_kd,
                double b_kp, double b_ki, double b_kd,
                ProportionalMode proportionalMode, Direction direction,
                double sampleTimeMS, double min, double max) {
        _FORWARD_KP = f_kp;
        _FORWARD_KI = f_ki;
        _FORWARD_KD = f_kd;
        _BACKWARD_KP = b_kp;
        _BACKWARD_KI = b_ki;
        _BACKWARD_KD = b_kd;

        _sourceInput = input;
        _sourceOutput = output;
        _sourceSetPoint = setPoint;
        _state = State.AUTOMATIC;
        setOutputLimits(min, max);
        _sampleTimeMS = sampleTimeMS;
        setControllerDirection(direction);
        _proportionalMode = proportionalMode;
        _lastTime = Robot.runtime.milliseconds() - _sampleTimeMS;
        _initializedLastInput = false;
    }

    public _PID(GetDouble input, SetDouble output, GetDouble setPoint,
                double kp, double ki, double kd,
                ProportionalMode proportionalMode, Direction direction,
                double sampleTimeMS, double min, double max) {
        this(input, output, setPoint, kp, ki, kd, kp, ki, kd, proportionalMode, direction, sampleTimeMS, min, max);
    }

    public boolean update() {
        if (_state == State.MANUAL) return false;
        double now = Robot.runtime.milliseconds();
        double timeChange = (now - _lastTime);
        if (timeChange >= _sampleTimeMS) {
            double input = _sourceInput.get();
            _sourceInputVal = _sourceInput.get();

            if (!_initializedLastInput) {
                _initializedLastInput = true;
                _lastInput = input;
            }

            double error = _sourceSetPoint.get() - input;
            double dInput = input - _lastInput;

            if (error >= 0) {
                setTunings(_FORWARD_KP, _FORWARD_KI, _FORWARD_KD, _proportionalMode);
            }
            else {
                setTunings(_BACKWARD_KP, _BACKWARD_KI, _BACKWARD_KD, _proportionalMode);
            }

            _outputSum += _ki * error;

            if (_proportionalMode == ProportionalMode.MEASUREMENT) _outputSum -= _kp * dInput;

            if (_outputSum > _outMax) _outputSum = _outMax;
            else if (_outputSum < _outMin) _outputSum = _outMin;

            double output;
            if (_proportionalMode == ProportionalMode.ERROR) output = _kp * error;
            else output = 0;

            output += _outputSum - _kd * dInput;
            if (output > _outMax) output = _outMax;
            else if (output < _outMin) output = _outMin;
            _sourceOutput.set(output);
            _sourceOutputVal = output;

            _lastInput = input;
            _lastTime = now;
            return true;
        }
        else return false;
    }

    public void setTunings(double kp, double ki, double kd, ProportionalMode proportionalMode) {
        if (kp < 0 || ki < 0 || kd < 0) return;

        _proportionalMode = proportionalMode;

        double sampleTimeInSec = _sampleTimeMS /1000.0;
        _kp = kp;
        _ki = ki * sampleTimeInSec;
        _kd = kd / sampleTimeInSec;

        if (_direction == Direction.REVERSE) {
            _kp *= -1;
            _ki *= -1;
            _kd *= -1;
        }
    }

    public void setSampleTime(double sampleTime) {
        if (sampleTime > 0) {
            double ratio = sampleTime / _sampleTimeMS;
            _ki *= ratio;
            _kd /= ratio;
            _sampleTimeMS = sampleTime;
        }
    }

    public void setOutputLimits(double min, double max) {
        if (min > max) return;
        _outMin = min;
        _outMax = max;

        if (_state == State.AUTOMATIC) {
            if (_sourceOutputVal > _outMax) {
                _sourceOutput.set(_outMax);
                _sourceOutputVal = _outMax;
            }
            else if (_sourceOutputVal < _outMin) {
                _sourceOutput.set(_outMin);
                _sourceOutputVal = _outMin;
            }

            if (_outputSum > _outMax) _outputSum = _outMax;
            else if (_outputSum < _outMin) _outputSum = _outMin;
        }
    }

    public void setState(State state) {
        if (state == State.AUTOMATIC && _state == State.MANUAL) {
            initialize();
        }
        _state = state;
    }

    public void initialize() {
        _lastInput = _sourceInput.get();
        _outputSum = 0;
        if (_outputSum > _outMax) _outputSum = _outMax;
        else if (_outputSum < _outMin) _outputSum = _outMin;
    }

    public void setControllerDirection(Direction direction) {
        if (_state == State.AUTOMATIC && direction != _direction) {
            _kp *= -1;
            _ki *= -1;
            _kd *= -1;
        }
        _direction = direction;
    }

    public double getLatestInputVal() {
        return _sourceInputVal;
    }

    public double getLatestOutputVal() {
        return _sourceOutputVal;
    }

    @FunctionalInterface
    public interface GetDouble {
        double get();
    }

    @FunctionalInterface
    public interface SetDouble {
        void set(double data);
    }

    public enum State {
        MANUAL,
        AUTOMATIC
    }

    public enum Direction {
        DIRECT,
        REVERSE
    }

    public enum ProportionalMode {
        MEASUREMENT,
        ERROR
    }
}