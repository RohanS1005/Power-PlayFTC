package org.firstinspires.ftc.teamcode.Auton;

        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

        import org.firstinspires.ftc.teamcode.Control.Robot;
        import org.firstinspires.ftc.teamcode.Control._Autonomous;
        import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

        import java.util.concurrent.TimeUnit;

@Autonomous(group="Auton", preselectTeleOp = "FinalTeleOp")
public class RedLeft extends _Autonomous {

    private State _state;
    private boolean _justEntered;
    private int _parkingSpot;
    double hue;
    double t2;

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

        _state = State.Move_Forward;
        _justEntered = true;
    }

    @Override
    public void loop() {
        Robot.update();
        Robot.getColor().fetchData();
        telemetry.addLine("Hue: " + String.valueOf(Robot.getColor().getHue()));
        hue = Robot.getColor().getHue();
        telemetry.addLine("Parking: " + String.valueOf(_parkingSpot));


        switch (_state) {
            case Move_Forward:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.2, 23, _Drivetrain.Movements.forward);
                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state = State.Sense;
                    _justEntered=true;
                }
                break;


            case Sense:
                if (_justEntered) {
                    _justEntered = false;
                    if (hue < 10 || hue > 350) {//red
                        _parkingSpot = 1;


                    } else if (hue > 110 && hue < 140) {//green
                        _parkingSpot = 2;


                    } else if (hue < 255 && hue > 195) {//blue
                        _parkingSpot = 3;

                    } else {
                        _parkingSpot = 4;
                    }


                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state = State.back;
                    _justEntered=true;
                }
                break;
           case back:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getDrivetrain().runDistance(0.25, 20, _Drivetrain.Movements.backward);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.wait6;
                    _justEntered = true;
                }
                break;

            case wait6:
                if(_justEntered){
                    _justEntered=false;
                    t2 = Robot.runtime.milliseconds();

                }
                else if(Robot.runtime.milliseconds() - t2 > 250){
                    _state = State.Moveright;
                    _justEntered=true;
                }
                break;

            case Moveright:
                if (_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.2, 28, _Drivetrain.Movements.right);
                }
                else if (!Robot.getDrivetrain().isBusy()){
                    _state=State.FORWARD_TO_LOW_POLE;
                    _justEntered=true;
                }

            case FORWARD_TO_LOW_POLE:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getDrivetrain().runDistance(0.3, 30, _Drivetrain.Movements.forward);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.Turnaxis;
                    _justEntered = true;
                }
                break;
            case Turnaxis:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.3, 5.2, _Drivetrain.Movements.cw);
                }
                else if (!Robot.getDrivetrain().isBusy()){
                    _state=State.RaiseSlide;
                    _justEntered=true;
                }
                break;
            case RaiseSlide:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getLinearslide().runDistance(0.5, 23 );
                    Robot.getDrivetrain().runDistance(0.3, 0.5, _Drivetrain.Movements.forward);
                }
                else if (!Robot.getLinearslide().isBusy()) {
                    _state = State.Turn_Claw;
                    _justEntered = true;
                }
                break;
            case Turn_Claw:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw6().setPosition(0.3);
                }
                else if(!Robot.getClaw6().isBusy()){
                    _state = State.wait2;
                    _justEntered=true;
                }
                break;

            case wait2:
                if(_justEntered){
                    _justEntered=false;
                    t2 = Robot.runtime.milliseconds();
                }
                else if(Robot.runtime.milliseconds() - t2 > 500){
                    _state = State.OPEN_CLAW;
                    _justEntered=true;
                }
                break;

            case OPEN_CLAW:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw().setPosition(0.55);
                }
                else if(!Robot.getClaw().isBusy()){
                    _state = State.wait3;
                    _justEntered=true;
                }
                break;

            case wait3:
                if(_justEntered){
                    _justEntered=false;
                    t2 = Robot.runtime.milliseconds();
                }
                else if(Robot.runtime.milliseconds() - t2 > 500){
                    _state = State.clawup;
                    _justEntered=true;
                }
                break;

            case clawup:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw6().setPosition(0);
                }
                else if(!Robot.getClaw6().isBusy()){
                    _state=State.wait4;
                    _justEntered=true;
                }
                break;
            case wait4:
                if(_justEntered){
                    _justEntered=false;
                    t2 = Robot.runtime.milliseconds();
                }
                else if(Robot.runtime.milliseconds() - t2 > 500){
                    _state = State.CLOSE_CLAW;
                    _justEntered=true;
                }
                break;

            case CLOSE_CLAW:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw().setPosition(0.8);
                }
                else if(!Robot.getClaw().isBusy()){
                    _state=State.wait5;
                    _justEntered=true;
                }
                break;

            case wait5:
                if(_justEntered){
                    _justEntered=false;
                    t2 = Robot.runtime.milliseconds();
                }
                else if(Robot.runtime.milliseconds() - t2 > 500){
                    _state = State.slidedown;
                    _justEntered=true;
                }
                break;

            case slidedown:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getLinearslide().runDistance(-0.5, 23 );
                }
                else if (!Robot.getLinearslide().isBusy()){
                    _state=State.turnback;
                    _justEntered=true;
                }
                break;
            case turnback:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.3, 5.3, _Drivetrain.Movements.ccw);
                }
                else if (!Robot.getDrivetrain().isBusy()){
                    _state=State.moveback1;
                    _justEntered=true;
                }
                break;
            case moveback1:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.3, 2.2, _Drivetrain.Movements.backward);
                }
                else if (!Robot.getDrivetrain().isBusy()){
                    _state=State.moveleft;
                    _justEntered=true;
                }
                break;

            case moveleft:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.3, 55, _Drivetrain.Movements.left);
                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state=State.Turnaxis3;
                    _justEntered=true;
                }
                break;

            case Turnaxis3:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.3, 1, _Drivetrain.Movements.ccw);
                }
                else if (!Robot.getDrivetrain().isBusy()){
                    _state=State.move_forward1;
                    _justEntered=true;
                }
                break;

            case move_forward1:
                if (_justEntered) {
                    _justEntered = false;
                    if (_parkingSpot == 2) {
                        Robot.getDrivetrain().runDistance(.3, 25, _Drivetrain.Movements.right);
                    } else if (_parkingSpot == 3) {

                        Robot.getDrivetrain().runDistance(.55, 50, _Drivetrain.Movements.right);
                    } else {
                    }
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.Turn_Right;
                    _justEntered = true;
                }
                break;
        }
    }

    private enum State {
        Moveright,
        FORWARD_TO_LOW_POLE,
        Turnaxis,
        RaiseSlide,
        CLOSE_CLAW,
        Turn_Claw,
        OPEN_CLAW,
        Turnaxis3,
        clawup,
        wait6,
        wait3,
        slidedown,
        wait4,
        turnback,
        moveback1,
        moveleft,
        wait5,
        Move_Forward,
        wait,
        wait2,
        Sense,
        back,
        FORWARD,
        move_forward1,
        Turn_Right,
    }
}
