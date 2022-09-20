package org.firstinspires.ftc.teamcode.Drivers;

public class _Drivetrain {

    private final int _MOTOR_NUM;
    private final double _Y_TO_X_RATIO;

    private final _Motor[] _drivetrain;
    private double[] _movement;
    private double _speed;
    private boolean _isBusy;

    public _Drivetrain(_Motor fr, _Motor fl, _Motor br, _Motor bl, double yToXRatio) {
        _MOTOR_NUM = 4;
        _Y_TO_X_RATIO = yToXRatio;
        _drivetrain = new _Motor[] {fr, fl, br, bl};
    }

    public void setTypicalSpeed(double speed) {
        _speed = speed;
        for (_Motor motor : _drivetrain) motor.setTypicalSpeed(_speed);
    }

    public void update() {
        for (_Motor motor : _drivetrain) motor.update();

        if (_isBusy) {
            boolean motorNotBusy = false;
            for (int i = 0; i < _MOTOR_NUM; ++i) {
                motorNotBusy = motorNotBusy || (!_drivetrain[i].isBusy() && _movement[i] != 0);
            }
            if (motorNotBusy) {
                resetForNextRun();
            }
        }
    }

    public void resetForNextRun() {
        _isBusy = false;
        for (_Motor motor : _drivetrain) motor.resetForNextRun();
    }

    public void runSpeed(double speed, double[] movement) {
        if (!_isBusy) {
            _movement = movement;
            for (int i = 0; i < _MOTOR_NUM; ++i)
                _drivetrain[i].runSpeed(speed * _movement[i]);
        }
    }

    public void runSpeed(double speed, Movements movement) {
        runSpeed(speed, movement.getDirections());
    }

    public void runSpeed(Movements movement) {
        runSpeed(_speed, movement);
    }

    public void runDistance(double speed, double distance, double[] movement) {
        if (!_isBusy && speed != 0) {
            _isBusy = true;
            _movement = movement;
            int sign = (speed < 0 || distance < 0 ? -1 : 1);
            for (int i = 0; i < _MOTOR_NUM; ++i)
                _drivetrain[i].runDistance(sign * Math.abs(speed) * _movement[i], Math.abs(distance));
        }
    }

    public void runDistance(double speed, double distance, Movements movement) {
        runDistance(speed, distance, movement.getDirections());
    }

    public void runDistance(double distance, Movements movement) {
        runDistance(_speed, distance, movement);
    }

    public void runTime(double speed, double milliseconds, double[] movement) {
        if (!_isBusy && speed != 0) {
            _isBusy = true;
            _movement = movement;
            for (int i = 0; i < _MOTOR_NUM; ++i)
                _drivetrain[i].runTime(speed * _movement[i], milliseconds);
        }
    }

    public void runTime(double speed, double milliseconds, Movements movement) {
        runTime(speed, milliseconds, movement.getDirections());
    }

    public void runTime(double milliseconds, Movements movement) {
        runTime(_speed, milliseconds, movement);
    }

    /* [DO NOT USE] NOT WORKING */
    public void runSpeedAngle(double speed, double degrees, double offsetDegrees, Movements movement) {
        if (!_isBusy) {
            _movement = movement.getDirections();
            double[] forwardDirections = Movements.forward.getDirections();
            double[] speeds = _anyDirection(speed, degrees, offsetDegrees);
            runSpeed(speed, new double[] {forwardDirections[0] * speeds[0] + _movement[0] * speed,
                    forwardDirections[1] * speeds[1] + _movement[1] * speed,
                    forwardDirections[2] * speeds[1] + _movement[2] * speed,
                    forwardDirections[3] * speeds[0] + _movement[3] * speed});
        }
    }

    public void runSpeedAngle(double speed, double degrees, double offsetDegrees) {
        if (!_isBusy) {
            _movement = Movements.forward.getDirections();
            double[] speeds = _anyDirection(speed, degrees, offsetDegrees);
            runSpeed(speed, new double[] {_movement[0] * speeds[0], _movement[1] * speeds[1], _movement[2] * speeds[1], _movement[3] * speeds[0]});
        }
    }

    public void runSpeedAngle(double degrees, double offsetDegrees) {
        runSpeedAngle(_speed, degrees, offsetDegrees);
    }

    public void stop() {
        for (int i = 0; i < _MOTOR_NUM; ++i) _drivetrain[i].stop();
    }

    public int getMotorNum() {
        return _MOTOR_NUM;
    }

    public double getSpeed() {
        return _speed;
    }

    public boolean isBusy() {
        return _isBusy;
    }

    private double[] _anyDirectionRadians(double speed, double radians, double offsetRadians) {
        double beta = Math.atan(_Y_TO_X_RATIO);

        double v1 = Math.sin(radians - (beta + offsetRadians));
        double v2 = Math.cos(radians - (beta + offsetRadians));

        double v1Max = speed * (v1 / Math.max(Math.abs(v1), Math.abs(v2)));
        double v2Max = speed * (v2 / Math.max(Math.abs(v1), Math.abs(v2)));

        return new double[] {v1Max, v2Max};
    }

    private double[] _anyDirection(double speed, double degrees, double offsetDegrees) {
        return _anyDirectionRadians(speed, Math.toRadians(degrees), Math.toRadians(offsetDegrees));
    }

    public enum Movements {
        // FR FL BR BL
        forward(1, -1, 1, -1),
        backward(-1, 1, -1, 1),
        left(1, 1, -1, -1),
        right(-1, -1, 1, 1),
        br(0, -1, 1, 0),
        bl(1, 0, 0, -1),
        tl(0, 1, -1, 0),
        tr(-1, 0, 0, 1),
        cw(-1, -1, -1, -1),
        ccw(1, 1, 1, 1),
        cwfront(-1, -1, 0, 0),
        ccwfront(1, 1, 0, 0),
        cwback(0, 0, -1, -1),
        ccwback(0, 0, 1, 1);

        private final double[] directions;

        Movements(double... signs) {
            this.directions = signs;
        }

        public double[] getDirections() {
            return directions;
        }
    }
}