package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="InverseKinematicsDemo", group="PeripheralTest")
public class InverseKinematicsDemo extends _Autonomous {
    @Override
    public void init(){
        Robot.setup(hardwareMap,telemetry, Robot.SetupType.CraneIMU,Robot.SetupType.CraneLift,Robot.SetupType.Bucket);
        Robot.setCraneLiftDegree(0);
        Robot.getBucket().setDegree(150);
    }

    @Override
    public void init_loop() {
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();
        Robot.getBucket().update();
    }

    @Override
    public void start() {
        Robot.setCraneLiftDegree(30);
        Robot.getBucket().setSlowDegree(210,2.5);
    }

    @Override
    public void loop(){
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();
        Robot.getBucket().update();
    }
}