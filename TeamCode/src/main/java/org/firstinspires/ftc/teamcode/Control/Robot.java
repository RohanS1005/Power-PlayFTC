package org.firstinspires.ftc.teamcode.Control;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;
import org.firstinspires.ftc.teamcode.Drivers._IMU;
import org.firstinspires.ftc.teamcode.Drivers._Motor;
import org.firstinspires.ftc.teamcode.Drivers._OpenCV;
import org.firstinspires.ftc.teamcode.Drivers._PID;
import org.firstinspires.ftc.teamcode.Drivers._Servo;
import org.firstinspires.ftc.teamcode.Drivers._ServoGroup;
import org.firstinspires.ftc.teamcode.Drivers._TFOD;
import org.firstinspires.ftc.teamcode.Drivers._Vuforia;

public final class Robot {

    public static ElapsedTime runtime;
    public static HardwareMap hardwareMap;
    public static Telemetry telemetry;

    private static _Drivetrain _drivetrain;
    private static _ServoGroup _bucket;
    private static _Motor _intake;
    private static _Motor _craneLift;
    private static _PID _craneLiftPID;
    private static _Motor _cranePivot;
    private static _PID _cranePivotPID;
    private static _Motor _carousel;
    private static _OpenCV _webcam;
    private static _Vuforia _vuforia;
    private static _TFOD _tfod;
    private static _IMU _imu;
    private static _IMU _craneIMU;

    public static final double MM_PER_INCH = 25.4;
    public static final double ANGLE_RANGE = 3;

    private static final double _TURN_OFFSET_POSITIVE = 18;
    private static final double _TURN_OFFSET_NEGATIVE = 15;
    private static final double _ENDGAME_CAROUSEL_SPEED = 0.5;
    private static final double _BUCKET_FLAT_ANGLE_OFFSET = 15.4;

    private static FieldSide _fieldSide;
    private static boolean _isTurning = false;
    private static double _startAngle;
    private static double _turnDegrees;
    private static double _craneLiftSetPoint;
    private static double _cranePivotSetPoint;
    private static double _CRANE_LIFT_ABOVE_CAROUSEL_DEGREE;
    private static _CRANE_TRANSITION_STATE _craneTransitionState = _CRANE_TRANSITION_STATE.INACTIVE;
    private static boolean _isCraneTransitioning = false;
    private static boolean _justEnteredCraneTransitionState;
    private static CranePreset _endCraneTransitionPreset;
    private static double _bucketAngleMaintainOffset;
    private static boolean _maintainBucketAngle = false;
    private static boolean _initialized = false;

    //Crane Presets
    public static CranePreset CRANE_COLLECTION_HOLD = new CranePreset(-70, 0, 80);
    public static CranePreset CRANE_COLLECTION_DROP = new CranePreset(-70, 0, 127);
    public static CranePreset CRANE_TOP_LEVEL_HOLD;
    public static CranePreset CRANE_TOP_LEVEL_DROP;
    public static CranePreset CRANE_MIDDLE_LEVEL_HOLD;
    public static CranePreset CRANE_MIDDLE_LEVEL_DROP;
    public static CranePreset CRANE_BOTTOM_LEVEL_HOLD;
    public static CranePreset CRANE_BOTTOM_LEVEL_DROP;
    public static CranePreset CRANE_SHARED_LEVEL_HOLD;
    public static CranePreset CRANE_SHARED_LEVEL_DROP;
    public static CranePreset CRANE_CAPPING_COLLECT = new CranePreset(-55, 180, 45);
    public static CranePreset CRANE_CAPPING_LIFT = new CranePreset(60, 180, 100);
    public static CranePreset CRANE_CAPPING_DROP = new CranePreset(17, 180, 100);

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
                case Bucket:
                    setupBucket(0);
                    break;
                case Intake:
                    setupIntake();
                    break;
                case CraneLift:
                    setupCraneLift();
                    break;
                case CranePivot:
                    setupCranePivot();
                    break;
                case Carousel:
                    setupCarousel();
                    break;
                case OpenCV:
                    setupOpenCV();
                    break;
                case Vuforia:
                    setupVuforia();
                    break;
                case TFOD:
                    setupTFOD();
                    break;
                case IMU:
                    setupIMU();
                    break;
                case CraneIMU:
                    setupCraneIMU();
                    break;
            }

            setupSequence.append(type.name()).append(" ");
        }

        telemetry.addLine(setupSequence.toString());
    }

    public static void setFieldSide(FieldSide fieldSide) {
        _fieldSide = fieldSide;

        if (_fieldSide == FieldSide.BLUE) {
            CRANE_TOP_LEVEL_HOLD = new CranePreset(46, 90, 180-22);
            CRANE_TOP_LEVEL_DROP = new CranePreset(46, 90, 180+46+35);
            CRANE_MIDDLE_LEVEL_HOLD = new CranePreset(-30, -90, 180-55);
            CRANE_MIDDLE_LEVEL_DROP = new CranePreset(10, -90, 220);
            CRANE_BOTTOM_LEVEL_HOLD = new CranePreset(-60, -90, 98);
            CRANE_BOTTOM_LEVEL_DROP = new CranePreset(-30, -90, 180);
            CRANE_SHARED_LEVEL_HOLD = new CranePreset(0, 140, 90);
            CRANE_SHARED_LEVEL_DROP = new CranePreset(0, 140, 210);
        }
        else if (_fieldSide == FieldSide.RED) {
            CRANE_TOP_LEVEL_HOLD = new CranePreset(46, -90, 180-22);
            CRANE_TOP_LEVEL_DROP = new CranePreset(46, -90, 180+46+35);
            CRANE_MIDDLE_LEVEL_HOLD = new CranePreset(-30, 90, 180-55);
            CRANE_MIDDLE_LEVEL_DROP = new CranePreset(10, 90, 220);
            CRANE_BOTTOM_LEVEL_HOLD = new CranePreset(-60, 90, 98);
            CRANE_BOTTOM_LEVEL_DROP = new CranePreset(-30, 90, 180);
            CRANE_SHARED_LEVEL_HOLD = new CranePreset(0, -140, 90);
            CRANE_SHARED_LEVEL_DROP = new CranePreset(0, -140, 210);
        }
        _CRANE_LIFT_ABOVE_CAROUSEL_DEGREE = 30;
    }

    private static void setupAutonomousPart1() {
        setupCraneIMU();
        setupCraneLift();
        setupBucket(0);
    }

    private static void setupAutonomousPart2() {
        setupIMU();
        setupCranePivot();
        setupDrivetrain();
        setupIntake();
        setupCarousel();
        //OpenCV is just for testing, not actual runs
    }

    private static void setupTeleOp1() {
        setupCraneIMU();
        setupCraneLift();
        setupBucket(CRANE_COLLECTION_DROP.BUCKET_DEGREE);
        setupCarousel();
    }

    private static void setupTeleOp2() {
        setupIMU();
        setupCranePivot();
        setupDrivetrain();
        setupIntake();
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

    private static void setupBucket(double startDegree) {
        _Servo left = new _Servo("bucketLeft", Servo.Direction.FORWARD, 0, 1, startDegree,
                0.17, 90, 0.51, 180);
        _Servo right = new _Servo("bucketRight", Servo.Direction.REVERSE, 0, 1, startDegree,
                0.17, 90, 0.51, 180);
        _bucket = new _ServoGroup(left, right);
    }

    private static void setupIntake() {
        _intake = new _Motor("intake", _Motor.Type.GOBILDA_435_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, false);
    }

    private static void setupCraneLift() {
        _craneLift = new _Motor("craneLift", _Motor.Type.GOBILDA_30_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, false);
        _craneLiftPID = new _PID(() -> _craneIMU.getRoll(), (double data) -> _craneLift.runSpeed(data), () -> _craneLiftSetPoint,
                0.008, 0.01, 0, 0.006, 0.006, 0,
                _PID.ProportionalMode.MEASUREMENT, _PID.Direction.DIRECT, 50, -1, 1);
    }

    private static void setupCranePivot() {
        _cranePivot = new _Motor("cranePivot", _Motor.Type.GOBILDA_117_RPM, DcMotorSimple.Direction.REVERSE,
                DcMotor.ZeroPowerBehavior.BRAKE, false);
        _cranePivotPID = new _PID(() -> (_craneIMU.getYaw() - _imu.getYaw()), (double data) -> _cranePivot.runSpeed(data), () -> _cranePivotSetPoint,
                0.0085, 0.015, 0, _PID.ProportionalMode.MEASUREMENT, _PID.Direction.DIRECT, 50, -1, 1);
    }

    private static void setupCarousel() {
        _carousel = new _Motor("carousel", _Motor.Type.GOBILDA_312_RPM, DcMotorSimple.Direction.FORWARD,
                DcMotor.ZeroPowerBehavior.BRAKE, true);
    }

    private static void setupOpenCV() {
        _webcam = new _OpenCV("Webcam 1", 320, 240);
    }

    public static void setupVuforia() {
        _vuforia = new _Vuforia("Webcam 1");
    }

    public static void setupTFOD() {
        _tfod = new _TFOD(_vuforia.getVuforia(), 0.45f, true, 320, 1.1, 16.0/9.0,
                "FreightFrenzy_BCDM.tflite", new String[] {"Ball", "Cube", "Duck", "Marker"});
    }

    private static void setupIMU() {
        _imu = new _IMU("imu", false, true);
    }

    private static void setupCraneIMU() {
        _craneIMU = new _IMU("armImu", 50, true, true);
    }

    public static void update() {
        telemetry.addLine("Update1");
        _imu.update();
        telemetry.addLine("Update2");
        _drivetrain.update();
        telemetry.addLine("Update3");
        _bucket.update();
        telemetry.addLine("Update4");
        _intake.update();
        telemetry.addLine("Update5");
        _craneIMU.update();
        telemetry.addLine("Update6");
        _craneLift.update();
        telemetry.addLine("Update7");
        _craneLiftPID.update();
        telemetry.addLine("Update8");
        _cranePivot.update();
        telemetry.addLine("Update9");
        _cranePivotPID.update();
        telemetry.addLine("Update10");
        _carousel.update();
        telemetry.addLine("Update11");

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

        if (_maintainBucketAngle) {
            getBucket().setDegree(90 + _craneIMU.getRoll() + _bucketAngleMaintainOffset);
        }
        telemetry.addLine("Update13");


        switch (_craneTransitionState) {
            case LIFT_CRANE:
                if (_justEnteredCraneTransitionState) {
                    _justEnteredCraneTransitionState = false;
                    setCraneLiftDegree(Math.max(_endCraneTransitionPreset.CRANE_LIFT_DEGREE, _CRANE_LIFT_ABOVE_CAROUSEL_DEGREE));
                }
                else if (_craneIMU.getRoll() >= _CRANE_LIFT_ABOVE_CAROUSEL_DEGREE - ANGLE_RANGE) {
                    _craneTransitionState = _CRANE_TRANSITION_STATE.PIVOT_CRANE;
                    _justEnteredCraneTransitionState = true;
                }
                break;
            case PIVOT_CRANE:
                if (_justEnteredCraneTransitionState) {
                    _justEnteredCraneTransitionState = false;
                    setCranePivotDegree(_endCraneTransitionPreset.CRANE_PIVOT_DEGREE);
                }
                else if ((_craneIMU.getYaw() - _imu.getYaw()) >= _endCraneTransitionPreset.CRANE_PIVOT_DEGREE - ANGLE_RANGE && (_craneIMU.getYaw() - _imu.getYaw()) <= _endCraneTransitionPreset.CRANE_PIVOT_DEGREE + ANGLE_RANGE) {
                    if (_endCraneTransitionPreset.CRANE_LIFT_DEGREE >= _CRANE_LIFT_ABOVE_CAROUSEL_DEGREE) {
                        _craneTransitionState = _CRANE_TRANSITION_STATE.LOWER_BUCKET;
                    }
                    else {
                        _craneTransitionState = _CRANE_TRANSITION_STATE.LOWER_CRANE;
                    }
                    _justEnteredCraneTransitionState = true;
                }
                break;
            case LOWER_CRANE:
                if (_justEnteredCraneTransitionState) {
                    _justEnteredCraneTransitionState = false;
                    setCraneLiftDegree(_endCraneTransitionPreset.CRANE_LIFT_DEGREE);
                }
                else if (_craneIMU.getRoll() >= _endCraneTransitionPreset.CRANE_LIFT_DEGREE - ANGLE_RANGE && _craneIMU.getRoll() <= _endCraneTransitionPreset.CRANE_LIFT_DEGREE + ANGLE_RANGE) {
                    _craneTransitionState = _CRANE_TRANSITION_STATE.LOWER_BUCKET;
                    _justEnteredCraneTransitionState = true;
                }
                break;
            case LOWER_BUCKET:
                if (_justEnteredCraneTransitionState) {
                    _justEnteredCraneTransitionState = false;
                    if (isMaintainingBucketAngle()) {
                        neglectBucketPosition();
                        getBucket().setSlowDegree(_endCraneTransitionPreset.BUCKET_DEGREE, 1000);
                    }
                    else {
                        getBucket().setDegree(_endCraneTransitionPreset.BUCKET_DEGREE);
                    }
                }
                else if (!getBucket().isBusy()) {
                    _craneTransitionState = _CRANE_TRANSITION_STATE.INACTIVE;
                    _justEnteredCraneTransitionState = true;
                }
                break;
            case INACTIVE:
                if (_justEnteredCraneTransitionState) {
                    _justEnteredCraneTransitionState = false;
                    _isCraneTransitioning = false;
                }
                break;
        }
        telemetry.addLine("Update14");
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

    public static void moveCraneToPreset(CranePreset cranePreset, boolean maintainBucketPosition) {
        _craneTransitionState = _CRANE_TRANSITION_STATE.LIFT_CRANE;
        _justEnteredCraneTransitionState = true;
        _isCraneTransitioning = true;
        _endCraneTransitionPreset = cranePreset;
        if (maintainBucketPosition) {
            maintainBucketPosition();
        }
    }

    public static boolean isCraneTransitioning() {
        return _isCraneTransitioning;
    }

    public static void maintainBucketPosition() {
        _maintainBucketAngle = true;
        telemetry.addLine("Update12");
        _bucketAngleMaintainOffset = _bucket.getDegree() - 90 - _craneIMU.getRoll();
    }

    public static boolean isMaintainingBucketAngle() {
        return _maintainBucketAngle;
    }

    public static void neglectBucketPosition() {
        _maintainBucketAngle = false;
    }

    public static void setCranePreset(CranePreset cranePreset) {
        setCraneLiftDegree(cranePreset.CRANE_LIFT_DEGREE);
        setCranePivotDegree(cranePreset.CRANE_PIVOT_DEGREE);
        getBucket().setSlowDegree(cranePreset.BUCKET_DEGREE, 2000);
    }

    public static void setCraneLiftDegree(double degree) {
        _craneLiftSetPoint = degree;
    }

    public static void setCranePivotDegree(double degree) {
        _cranePivotSetPoint = degree;
    }

    public static _Drivetrain getDrivetrain() {
        return _drivetrain;
    }

    public static _ServoGroup getBucket() {
        return _bucket;
    }

    public static _Motor getIntake() {
        return _intake;
    }

    public static _PID getCraneLiftPID() {
        return _craneLiftPID;
    }

    public static _Motor getCraneLift() {
        return _craneLift;
    }

    public static _PID getCranePivotPID() {
        return _cranePivotPID;
    }

    public static _Motor getCranePivot() {
        return _cranePivot;
    }

    public static _Motor getCarousel() {
        return _carousel;
    }

    public static _OpenCV getWebcam() {
        return _webcam;
    }

    public static _Vuforia getVuforia() {
        return _vuforia;
    }

    public static _TFOD getTFOD() {
        return _tfod;
    }

    public static _IMU getIMU() {
        return _imu;
    }

    public static _IMU getCraneIMU() {
        return _craneIMU;
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
        Bucket,
        Intake,
        CraneLift,
        CranePivot,
        Carousel,
        OpenCV,
        Vuforia,
        TFOD,
        IMU,
        CraneIMU
    }

    public enum FieldSide {
        BLUE,
        RED
    }

    private enum _CRANE_TRANSITION_STATE {
        LIFT_CRANE,
        PIVOT_CRANE,
        LOWER_CRANE,
        LOWER_BUCKET,
        INACTIVE
    }

    public enum TurnAxis {
        Front,
        Center,
        Back
    }

    public static class CranePreset {
        public final double CRANE_LIFT_DEGREE;
        public final double CRANE_PIVOT_DEGREE;
        public final double BUCKET_DEGREE;

        public CranePreset(double craneLiftDegree, double cranePivotDegree, double bucketDegree) {
            CRANE_LIFT_DEGREE = craneLiftDegree;
            CRANE_PIVOT_DEGREE = cranePivotDegree;
            BUCKET_DEGREE = bucketDegree;
        }
    }
}