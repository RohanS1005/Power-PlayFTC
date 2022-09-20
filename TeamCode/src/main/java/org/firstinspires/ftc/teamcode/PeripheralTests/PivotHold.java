package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="PivotHold", group="PeripheralTest")
public class PivotHold extends _Autonomous {
    @Override
    public void init(){
        Robot.setup(hardwareMap,telemetry, Robot.SetupType.CraneIMU,Robot.SetupType.CraneLift,Robot.SetupType.Bucket);
        Robot.setCraneLiftDegree(45);
        Robot.getBucket().setDegree(215);
    }
    @Override
    public void init_loop(){
        Robot.getCraneIMU().update();
        Robot.getCraneLift().update();
        Robot.getCraneLiftPID().update();
        Robot.getBucket().update();
    }

    @Override
    public void loop() {}
}