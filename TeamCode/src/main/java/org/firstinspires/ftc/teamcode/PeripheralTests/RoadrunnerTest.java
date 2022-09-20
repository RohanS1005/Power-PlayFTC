package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.advanced.PoseStorage;

@Autonomous(group = "PeripheralTest")
public class RoadrunnerTest extends LinearOpMode {

    private State _state = State.SPLINE_FORWARD;

    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

//        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
//                .strafeRight(10)
//                .build();

//        Trajectory traj2 = drive.trajectoryBuilder(traj1.end())
//                .forward(5)
//                .build();

        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d(), true)
                .splineToConstantHeading(new Vector2d(0, 24), 0)
                .build();
        Trajectory traj2 = drive.trajectoryBuilder(traj1.end(), true)
                .splineToConstantHeading(new Vector2d(-48, 24), 0)
                .build();

        waitForStart();

        if(isStopRequested()) return;

        drive.followTrajectoryAsync(traj1);
        while (opModeIsActive() && !isStopRequested()) {
            switch (_state) {
                case SPLINE_FORWARD:
                    if (!drive.isBusy()) {
                        _state = State.SPLINE_BACKWARD;
                        drive.followTrajectory(traj2);
                    }
                    break;
                case SPLINE_BACKWARD:
                    if (!drive.isBusy()) {
                        _state = State.IDLE;
                    }
                    break;
                case IDLE:
                    break;
            }

            drive.update();

            PoseStorage.currentPose = drive.getPoseEstimate();
        }
    }

    private enum State {
        SPLINE_FORWARD,
        SPLINE_BACKWARD,
        IDLE
    }
}
