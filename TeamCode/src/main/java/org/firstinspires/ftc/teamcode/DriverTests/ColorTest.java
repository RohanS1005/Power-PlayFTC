package org.firstinspires.ftc.teamcode.DriverTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="ColorTest", group="DriverTest")
public class ColorTest extends _Autonomous {

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.Color);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        Robot.getColor().fetchData();
        telemetry.addLine(Robot.getColor().getName());
        telemetry.addLine("Red: " + String.valueOf(Robot.getColor().getRed()));
        telemetry.addLine("Blue: " + String.valueOf(Robot.getColor().getBlue()));
        telemetry.addLine("Green: " + String.valueOf(Robot.getColor().getGreen()));
        telemetry.addLine("Hue: " + String.valueOf(Robot.getColor().getHue()));
        telemetry.addLine("Sat: " + String.valueOf(Robot.getColor().getSat()));
        telemetry.addLine("Val: " + String.valueOf(Robot.getColor().getVal()));
    }
}