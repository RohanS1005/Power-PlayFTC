package org.firstinspires.ftc.teamcode.PeripheralTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;

@Autonomous(name="LinearslideTest", group="DriverTest")
public class LinearslideTest extends _Autonomous {

    private States _state;
    private boolean _justEntered;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.Linearslide);
        _state = States.RUN_DIST;
        _justEntered = true;
    }

    @Override
    public void loop() {
        Robot.getLinearslide().update();
        telemetry.addLine(_state.name());
        telemetry.addLine("Encoder Counts: " + Robot.getLinearslide().getCounts());

        switch (_state) {
            case RUN_DIST:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getLinearslide().runDistance(0.1, 2);
                }
                else if (!Robot.getLinearslide().isBusy()) {
                    _state = States.STOP;
                    _justEntered = true;
                }
                break;
            case STOP:
                break;
        }
    }

    private enum States {
        RUN_DIST,
        STOP
    }
}