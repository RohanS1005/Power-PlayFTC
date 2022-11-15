package org.firstinspires.ftc.teamcode.Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

@Autonomous(group="Auton", preselectTeleOp = "FinalTeleOp")
public class RedLeft extends _Autonomous {

    private State _state;
    private boolean _justEntered;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.AutonomousPart1);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.AutonomousPart2);

        _state = State.FORWARD_TO_LOW_POLE;
        _justEntered = true;
    }

    @Override
    public void loop() {
        Robot.update();

        switch (_state) {
            case FORWARD_TO_LOW_POLE:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.forward);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.RAISE_SLIDE;
                    _justEntered = true;
                }
                break;
            case RAISE_SLIDE:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getLinearslide().runDistance(0.5, 10);
                }
                else if (!Robot.getLinearslide().isBusy()) {
                    _state = State.ROTATE_CLAW;
                    _justEntered = true;
                }
        }
    }

    private enum State {
        FORWARD_TO_LOW_POLE,
        RAISE_SLIDE,
        ROTATE_CLAW,
        OPEN_CLAW
    }
}