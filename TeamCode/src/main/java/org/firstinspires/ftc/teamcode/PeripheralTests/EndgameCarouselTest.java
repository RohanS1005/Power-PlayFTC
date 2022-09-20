package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="EndgameCarouselTest", group="PeripheralTest")
public class EndgameCarouselTest extends _Autonomous {

    @Override
    public void init(){
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.Carousel);
        Robot.getCarousel().setTypicalSpeed(0.5);
    }

    @Override
    public void loop(){
        Robot.getCarousel().update();
        Robot.getCarousel().runTime(10000);
    }
}
