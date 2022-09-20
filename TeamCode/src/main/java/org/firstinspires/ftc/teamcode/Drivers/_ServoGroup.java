package org.firstinspires.ftc.teamcode.Drivers;

public class _ServoGroup {

    private final int _SERVO_NUM;

    private final _Servo[] _servos;

    public _ServoGroup(_Servo... servos) {
        _SERVO_NUM = servos.length;
        _servos = servos;
    }

    public void update() {
        for (_Servo servo : _servos) {
            servo.update();
        }
    }

    public void resetForNextRun() {
        for (_Servo servo : _servos) {
            servo.resetForNextRun();
        }
    }

    public void setPosition(double position) {
        if (!isBusy()) {
            for (_Servo servo : _servos) {
                servo.setPosition(position);
            }
        }
    }

    public void setDegree(double degree) {
        if (!isBusy()) {
            for (_Servo servo : _servos) {
                servo.setDegree(degree);
            }
        }
    }

    public void setSlowPosition(double position, double durationMS) {
        if (!isBusy()) {
            for (_Servo servo : _servos) {
                servo.setSlowPosition(position, durationMS);
            }
        }
    }

    public void setSlowDegree(double degree, double durationMS) {
        if (!isBusy()) {
            for (_Servo servo : _servos) {
                servo.setSlowDegree(degree, durationMS);
            }
        }
    }

    public int getServoNum() {
        return _SERVO_NUM;
    }

    public double getPosition() {
        return _servos[0].getPosition();
    }

    public double getDegree() {
        return _servos[0].getDegree();
    }

    public boolean isBusy() {
        boolean isBusy = false;
        for (_Servo servo : _servos) {
            isBusy = isBusy || servo.isBusy();
        }
        return isBusy;
    }
}