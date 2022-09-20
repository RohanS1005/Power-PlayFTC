package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="OdometryTest", group="PeripheralTest")
public class OdometryTest extends _Autonomous {

    private int _leftCounts;
    private int _middleCounts;
    private int _rightCounts;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.CranePivot, Robot.SetupType.CraneLift, Robot.SetupType.Intake);
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        _leftCounts = Robot.getCraneLift().getCounts();
        _middleCounts = Robot.getCranePivot().getCounts();
        _rightCounts = Robot.getIntake().getCounts();
    }

    @Override
    public void loop() {
        telemetry.addLine("Left: " + (Robot.getCraneLift().getCounts() - _leftCounts));
        telemetry.addLine("Middle: " + (Robot.getCranePivot().getCounts() - _middleCounts));
        telemetry.addLine("Right: " + (Robot.getIntake().getCounts() - _rightCounts));
    }

    @Override
    public void stop() {}
}