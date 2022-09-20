package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="IntakeTest", group="PeripheralTest")
public class IntakeTest extends _Autonomous {

    @Override
    public void init(){
        Robot.setup(hardwareMap,telemetry, Robot.SetupType.Intake);
        Robot.getIntake().setTypicalSpeed(-0.8);
    }

    @Override
    public void loop(){
        Robot.getIntake().update();
        Robot.getIntake().runTime(5000);
    }
}
