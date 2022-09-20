package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

@Autonomous(group="Auton", preselectTeleOp = "FinalTeleOpBlue")
public class BlueAutonWarehouse extends _Autonomous {

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.Drivetrain);
    }

    @Override
    public void start() {
        Robot.getDrivetrain().runDistance(1.0, 40, _Drivetrain.Movements.forward);
    }

    @Override
    public void loop() {
        Robot.getDrivetrain().update();
    }
}