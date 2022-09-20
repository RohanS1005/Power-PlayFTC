package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._IMU;

@Autonomous(name="CraneIMUTest", group="PeripheralTest")
public class CraneIMUTest extends _Autonomous {

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.CraneIMU);
    }

    @Override
    public void init_loop() {
        Robot.getCraneIMU().update();
        telemetry.addLine(Robot.getCraneIMU().getName());
        telemetry.addLine(String.valueOf(Robot.getCraneIMU().getYaw()));
        telemetry.addLine(String.valueOf(Robot.getCraneIMU().getPitch()));
        telemetry.addLine(String.valueOf(Robot.getCraneIMU().getRoll()));
    }

    @Override
    public void loop() {}
}