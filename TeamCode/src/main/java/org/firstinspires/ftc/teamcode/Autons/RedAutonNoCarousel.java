package org.firstinspires.ftc.teamcode.Autons;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._TFOD;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.advanced.PoseStorage;

import java.util.List;

@Autonomous(group="Auton")
public class RedAutonNoCarousel extends _Autonomous {

    private _TFOD.ValidRecognition _validRecognition;
    private List<Recognition> _recognitions;
    private TeamElementLocations _location;
    private boolean _tfodRight = false;
    private SampleMecanumDrive _drive;
    private Trajectory _toCarousel;
    private Trajectory _toAllianceHub;
    private Trajectory _approachHub;
    private Trajectory _toWall;
    private Trajectory _toWarehouse;
    private Trajectory _toLeft;
    private Trajectory _toDeep;
    private State _state;
    private boolean _justEntered;
    private Robot.CranePreset _dropPreset;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.AutonomousPart1);
        Robot.setFieldSide(Robot.FieldSide.BLUE); //Auton flip
        Robot.getBucket().setDegree(45);
        Robot.setCraneLiftDegree(Robot.CRANE_COLLECTION_HOLD.CRANE_LIFT_DEGREE);

        _validRecognition = recognition ->
                ((recognition.getWidth() * recognition.getHeight()) < (320.0 * 640.0 / 8.0))
                        && (recognition.getWidth()/recognition.getHeight()) > 1.0;
        _justEntered = true;
    }

    @Override
    public void init_loop() {
        if (_justEntered) {
            _justEntered = false;
            Robot.setupVuforia();
            Robot.setupTFOD();
        }

        Robot.getBucket().update();
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();

        _recognitions = Robot.getTFOD().getLatestRecognitions();
        if (_recognitions == null) {
            _location = _tfodRight ? TeamElementLocations.LEFT : TeamElementLocations.RIGHT;
        }
        else if (_recognitions.size() == 0) {
            _location = _tfodRight ? TeamElementLocations.LEFT : TeamElementLocations.RIGHT;
        }
        else {
            if (Robot.getTFOD().countValidLabel(_validRecognition, "Marker") == 1) {
                if ((Robot.getTFOD().getRecognitionValidLabel(_validRecognition, "Marker").getLeft()
                        + Robot.getTFOD().getRecognitionValidLabel(_validRecognition, "Marker").getRight())/2.0 > 320) {
                    _location = _tfodRight ? TeamElementLocations.MIDDLE : TeamElementLocations.LEFT;
                }
                else {
                    _location = _tfodRight ? TeamElementLocations.RIGHT : TeamElementLocations.MIDDLE;
                }
            }
            else {
                _location = _tfodRight ? TeamElementLocations.LEFT : TeamElementLocations.RIGHT;
            }
        }

        telemetry.addLine("Bucket: " + Robot.getBucket().getDegree());
        telemetry.addLine("Lift: " + Robot.getCraneIMU().getRoll());
        telemetry.addLine(_location != null ? _location.name() : "null");
        telemetry.addLine(_recognitions != null ? _recognitions.toString() : "null");
    }

    @Override
    public void start() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.AutonomousPart2);
        Robot.getTFOD().deactivate();
        Robot.getIMU().willUpdate(true);
        telemetry.addLine("Bucket: " + Robot.getBucket().getDegree());
        telemetry.addLine("Lift: " + Robot.getCraneIMU().getRoll());
        telemetry.addLine("Pivot: " + Robot.getCraneIMU().getYaw());
        telemetry.addLine("IMU: " + Robot.getIMU().getYaw());
        telemetry.addLine("PivotPID: " + Robot.getCranePivotPID().getLatestInputVal());

        _drive = new SampleMecanumDrive(hardwareMap);

        _toAllianceHub = _drive.trajectoryBuilder(new Pose2d())
                .splineTo(new Vector2d(0, 19), Math.toRadians(90),
                        SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        _approachHub = _drive.trajectoryBuilder(_toAllianceHub.end())
                .forward(20,
                        SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        _toWall = _drive.trajectoryBuilder(_approachHub.end())
                .strafeLeft(21,
                        SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        _toWarehouse = _drive.trajectoryBuilder(_toWall.end())
                .strafeLeft(48,
                        SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        _toLeft = _drive.trajectoryBuilder(_toWarehouse.end())
                .strafeRight(24,
                        SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        _toDeep = _drive.trajectoryBuilder(_toLeft.end())
                .back(24,
                        SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        _state = State.APPROACH_HUB;
        _justEntered = true;
    }

    @Override
    public void loop() {
        Robot.update();
        _drive.update();
        PoseStorage.currentPose = _drive.getPoseEstimate();

        switch (_state) {
            case MOVE_TO_ALLIANCE_HUB_PLUS_CRANE:
                if (_justEntered) {
                    _justEntered = false;
                    _drive.followTrajectoryAsync(_toAllianceHub);

                    switch (_location) {
                        case RIGHT:
                            Robot.moveCraneToPreset(Robot.CRANE_TOP_LEVEL_HOLD, true);
                            _dropPreset = Robot.CRANE_TOP_LEVEL_DROP;
                            break;
                        case MIDDLE:
                            Robot.moveCraneToPreset(Robot.CRANE_MIDDLE_LEVEL_HOLD, true);
                            _dropPreset = Robot.CRANE_MIDDLE_LEVEL_DROP;
                            break;
                        case LEFT:
                            Robot.moveCraneToPreset(Robot.CRANE_BOTTOM_LEVEL_HOLD, true);
                            _dropPreset = Robot.CRANE_BOTTOM_LEVEL_DROP;
                            break;
                    }
                }
                else if (!_drive.isBusy() && !Robot.isCraneTransitioning()) {
                    _state = State.APPROACH_HUB;
                    _justEntered = true;
                }
                break;
            case APPROACH_HUB:
                if (_justEntered) {
                    _justEntered = false;
                    _drive.followTrajectoryAsync(_approachHub);
                }
                else if (!_drive.isBusy()) {
                    _state = State.IDLE;
                    _justEntered = true;
                }
                break;
            case DROP_PRELOADED_CUBE:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.setCraneLiftDegree(_dropPreset.CRANE_LIFT_DEGREE);
                    Robot.getBucket().setSlowDegree(_dropPreset.BUCKET_DEGREE, 1000);
                }
                else if (Robot.getCraneIMU().getRoll() >= _dropPreset.CRANE_LIFT_DEGREE - Robot.ANGLE_RANGE
                        && Robot.getCraneIMU().getRoll() <= _dropPreset.CRANE_LIFT_DEGREE + Robot.ANGLE_RANGE
                        && !Robot.getBucket().isBusy()) {
                    _state = State.MOVE_TO_WALL;
                    _justEntered = true;
                }
                break;
            case MOVE_TO_WALL:
                if (_justEntered) {
                    _justEntered = false;
                    _drive.followTrajectoryAsync(_toWall);
                }
                else if (!_drive.isBusy()) {
                    _state = State.MOVE_TO_WAREHOUSE;
                    _justEntered = true;
                }
                break;
            case MOVE_TO_WAREHOUSE:
                if (_justEntered) {
                    _justEntered = false;
                    _drive.followTrajectoryAsync(_toWarehouse);
//                    Robot.getBucket().setDegree(Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE);
//                    Robot.moveCraneToPreset(Robot.CRANE_COLLECTION_HOLD, false);
                }
                else if (!_drive.isBusy() /* && !Robot.isCraneTransitioning()*/) {
                    _state = State.MOVE_LEFT_WAREHOUSE;
                    _justEntered = true;
                }
                break;
            case MOVE_LEFT_WAREHOUSE:
                if (_justEntered) {
                    _justEntered = false;
                    _drive.followTrajectoryAsync(_toLeft);
//                    Robot.getBucket().setSlowDegree(Robot.CRANE_COLLECTION_DROP.BUCKET_DEGREE, 1000);
//                    Robot.getIntake().runSpeed(0.3);
                }
                else if (!_drive.isBusy()/* && !Robot.getBucket().isBusy()*/) {
                    _state = State.MOVE_DEEP;
                    _justEntered = true;
                    Robot.getIntake().stop();
                }
                break;
            case MOVE_DEEP:
                if (_justEntered) {
                    _justEntered = false;
                    _drive.followTrajectoryAsync(_toDeep);
                }
                else if (!_drive.isBusy()) {
                    _state = State.TURN_180;
                    _justEntered = true;
                }
                break;
            case TURN_180:
                if (_justEntered) {
                    _justEntered = false;
                    _drive.turn(Math.toRadians(-90));
                }
                else if (!_drive.isBusy()) {
                    _state = State.IDLE;
                    _justEntered = true;
                }
            case IDLE:
                break;
        }
    }

    private enum TeamElementLocations {
        LEFT,
        MIDDLE,
        RIGHT
    }

    private enum State {
        MOVE_TO_ALLIANCE_HUB_PLUS_CRANE,
        APPROACH_HUB,
        DROP_PRELOADED_CUBE,
        MOVE_TO_WALL,
        MOVE_TO_WAREHOUSE,
        MOVE_LEFT_WAREHOUSE,
        MOVE_DEEP,
        TURN_180,
        IDLE
    }
}