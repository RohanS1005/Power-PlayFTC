package org.firstinspires.ftc.teamcode.PeripheralTests;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
@Autonomous(name="BucketTest", group="PeripheralTest")
public class BucketTest extends _Autonomous{

    @Override
    public void init(){
        Robot.setup(hardwareMap,telemetry,Robot.SetupType.Bucket, Robot.SetupType.Intake);
        Robot.getIntake().setTypicalSpeed(-0.2);
        Robot.getBucket().setDegree(145);

    }
    @Override
    public void loop(){
        Robot.getBucket().update();
        Robot.getIntake().update();
        Robot.getIntake().runTime(5000);
        Robot.getBucket().setDegree(145);
    }
}
