package org.firstinspires.ftc.teamcode.Control;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;
import org.firstinspires.ftc.teamcode.Drivers._IMU;
import org.firstinspires.ftc.teamcode.Drivers._Color;


import org.firstinspires.ftc.teamcode.Drivers._Motor;
import org.firstinspires.ftc.teamcode.Drivers._Servo;

public final class Robot {

    public static ElapsedTime runtime;
    public static HardwareMap hardwareMap;
    public static Telemetry telemetry;

    private static _Drivetrain _drivetrain;
    private static _IMU _imu;
    private static _Color _color;
    private static _Motor _linearslide;
    private static _Servo _claw;
    private static _Servo _claw6;

    public static final double MM_PER_INCH = 25.4;
    public static final double ANGLE_RANGE = 3;

    private static final double _TURN_OFFSET_POSITIVE = 18;
    private static final double _TURN_OFFSET_NEGATIVE = 15;

    private static final double _LEVEL1 = 1135;
    private static final double _LEVEL2 = 2999;
    private static final double _Level3 = 4701;

    private static FieldSide _fieldSide;
    private static boolean _isTurning = false;
    private static double _startAngle;
    private static double _turnDegrees;
    private static boolean _initialized = false;

    private Robot() {};

    public static void setup(HardwareMap centralHardwareMap, Telemetry centralTelemetry, SetupType... setupTypes) {
        if (!_initialized) {
            _initialized = true;
            runtime = new ElapsedTime();
            hardwareMap = centralHardwareMap;
            telemetry = centralTelemetry;
        }

        StringBuilder setupSequence = new StringBuilder();
        for (SetupType type : setupTypes) {
            switch(type) {
                case AutonomousPart1:
                    setupAutonomousPart1();
                    break;
                case AutonomousPart2:
                    setupAutonomousPart2();
                    break;
                case TeleOp1:
                    setupTeleOp1();
                    break;
                case TeleOp2:
                    setupTeleOp2();
                    break;
                case Drivetrain:
                    setupDrivetrain();
                    break;
                case IMU:
                    setupIMU();
                    break;
                case Linearslide:
                    setupLinearslide();
                    break;
                case Claw:
                    setupClaw();
                    break;
                case Claw6:
                    setupClaw6();
                    break;
                case Color:
                    setupColor();
                    break;
            }

            setupSequence.append(type.name()).append(" ");
        }

        telemetry.addLine(setupSequence.toString());
    }

    public static void setFieldSide(FieldSide fieldSide) {
        _fieldSide = fieldSide;

        if (_fieldSide == FieldSide.BLUE) {
        }
        else if (_fieldSide == FieldSide.RED) {
        }
    }

    private static void setupAutonomousPart1() {
    }

    private static void setupAutonomousPart2() {
        setupIMU();
        setupDrivetrain();
        setupLinearslide();
        setupClaw();
        setupClaw6();
        //OpenCV is just for testing, not actual runs
    }

    private static void setupTeleOp1() {
    }

    private static void setupTeleOp2() {
        setupIMU();
        setupDrivetrain();
        setupLinearslide();
        setupClaw();
        setupClaw6();
        //OpenCV is just for testing, not actual runs
    }

    private static void setupDrivetrain() {
        double wheelDiameter = 96/MM_PER_INCH;
        _Motor fr = new _Motor("motorFR", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _Motor fl = new _Motor("motorFL", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _Motor br = new _Motor("motorBR", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _Motor bl = new _Motor("motorBL", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, wheelDiameter, true);
        _drivetrain = new _Drivetrain(fr, fl, br, bl, 1.0);
    }

    private static void setupIMU() {
        _imu = new _IMU("imu", false, true);
    }

    private static void setupColor() {
        _color = new _Color("color");
    }

    private static void setupLinearslide() {
        double linearslideDiameter = 1.25/2; //inches
        _linearslide = new _Motor("linearslide", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.REVERSE,
                DcMotor.ZeroPowerBehavior.BRAKE, linearslideDiameter, true); //Add encoder if theres isn't already
    }

    private static void setupClaw() {
        double startPosition = 1;
        _claw = new _Servo("claw", Servo.Direction.REVERSE, 0, 1, startPosition);

    }
    private static void setupClaw6() {
        double startPosition = 0;
        _claw6 = new _Servo("claw6", Servo.Direction.FORWARD, 0, 1, startPosition);
    }

    public static void update() {
        telemetry.addLine("Update1");
        _imu.update();
        telemetry.addLine("Update2");
        _drivetrain.update();
        telemetry.addLine("Update3");
        _linearslide.update();
        _claw.update();
        _claw6.update();

        if (_isTurning) {
            if (Math.abs(_turnDegrees) > Math.max(_TURN_OFFSET_POSITIVE, _TURN_OFFSET_NEGATIVE)) {
                if (_turnDegrees > 0 ? _imu.getYaw() - _startAngle >= _turnDegrees - _TURN_OFFSET_POSITIVE : _imu.getYaw() - _startAngle <= _turnDegrees + _TURN_OFFSET_NEGATIVE) {
                    _isTurning = false;
                }
            }
            else {
                if (_turnDegrees > 0 ? _imu.getYaw() - _startAngle >= _turnDegrees : _imu.getYaw() - _startAngle <= _turnDegrees) {
                    _isTurning = false;
                }
            }

            if (!_isTurning) {
                _drivetrain.stop();
            }
        }
        telemetry.addLine("Update12");
    }

    public static void turn(double speed, double degrees, TurnAxis turnAxis) {
        if (!_isTurning && degrees != 0) {
            _isTurning = true;
            _startAngle = _imu.getYaw();
            degrees = _turnDegrees;

            switch (turnAxis) {
                case Center:
                    _drivetrain.runSpeed(speed, degrees > 0 ? _Drivetrain.Movements.cw : _Drivetrain.Movements.ccw);
                    break;
                case Back:
                    _drivetrain.runSpeed(speed, degrees > 0 ? _Drivetrain.Movements.cwback : _Drivetrain.Movements.ccwback);
                    break;
                case Front:
                    _drivetrain.runSpeed(speed, degrees > 0 ? _Drivetrain.Movements.cwfront : _Drivetrain.Movements.ccwfront);
                    break;
            }
        }
    }

    public static _Drivetrain getDrivetrain() {
        return _drivetrain;
    }

    public static _IMU getIMU() {
        return _imu;
    }

    public static _Color getColor() {
        return _color;
    }

    public static _Motor getLinearslide() {
        return _linearslide;
    }

    public static _Servo getClaw() {
        return _claw;
    }

    public static _Servo getClaw6() {
        return _claw6;
    }

    public static boolean isTurning() {
        return _isTurning;
    }

    public enum SetupType {
        AutonomousPart1,
        AutonomousPart2,
        TeleOp1,
        TeleOp2,
        Drivetrain,
        IMU,
        Linearslide,
        Claw,
        Claw6,
        Color
    }

    public enum FieldSide {
        BLUE,
        RED
    }

    public enum TurnAxis {
        Front,
        Center,
        Back
    }
}