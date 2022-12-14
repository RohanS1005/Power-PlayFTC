package org.firstinspires.ftc.teamcode.Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

import java.util.concurrent.TimeUnit;

@Autonomous(group="Auton", preselectTeleOp = "FinalTeleOp")
public class BlueRight extends _Autonomous {

    private BlueRight.State _state;
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

        _state = State.lilforward;
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
                    _state = State.Wait;
                    _justEntered=true;
                }
                break;

            case Wait:
                if(_justEntered){
                    _justEntered=false;
                    double t2 = Robot.runtime.milliseconds();



                }
                else if(Robot.runtime.milliseconds() - t2 > 4000){
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
                    Robot.getDrivetrain().runDistance(0.2, 20, _Drivetrain.Movements.backward);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.Moveright;
                    _justEntered = true;
                }
                break;
            case Moveright:
                if (_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.3, 28, _Drivetrain.Movements.left);
                }
                else if (!Robot.getDrivetrain().isBusy()){
                    _state= State.FORWARD_TO_LOW_POLE;
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
                    Robot.getDrivetrain().runDistance(0.3, 5, _Drivetrain.Movements.ccw);
                }
                else if (!Robot.getDrivetrain().isBusy()){
                    _state= State.RaiseSlide;
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
                    _state = State.OPEN_CLAW;
                    _justEntered=true;
                }
                break;
            case OPEN_CLAW:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw().setPosition(0.85);
                }
                else if(!Robot.getClaw().isBusy()){
                    _state = State.clawup;
                    _justEntered=true;
                }
                break;
            case clawup:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw6().setPosition(0);
                    Robot.getClaw().setPosition(1);
                }
                else if(!Robot.getClaw6().isBusy()){
                    _state= State.slidedown;
                    _justEntered=true;
                }
                break;
            case slidedown:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getLinearslide().runDistance(-0.5, 23 );
                }
                else if (!Robot.getLinearslide().isBusy()){
                    _state= State.turnback;
                    _justEntered=true;
                }
                break;
            case turnback:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.3, 5, _Drivetrain.Movements.cw);
                }
                else if (!Robot.getDrivetrain().isBusy()){
                    _state= State.moveback1;
                    _justEntered=true;
                }
                break;
            case moveback1:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.3, 5, _Drivetrain.Movements.backward);
                }
                else if (!Robot.getDrivetrain().isBusy()){
                    _state= State.moveleft;
                    _justEntered=true;
                }
                break;

            case moveleft:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.3, 55, _Drivetrain.Movements.right);
                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state= State.move_forward1;
                    _justEntered=true;
                }
                break;

            case move_forward1:
                if (_justEntered) {
                    _justEntered = false;
                    if (_parkingSpot == 2) {
                        Robot.getDrivetrain().runDistance(0.5, 25, _Drivetrain.Movements.right);
                    } else if (_parkingSpot == 1) {

                        Robot.getDrivetrain().runDistance(0.5, 50, _Drivetrain.Movements.right);
                    } else {
                    }
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.Turn_Right;
                    _justEntered = true;
                }
                break;
//            case Turn_Right:
//                if (_justEntered) {
//                    _justEntered = false;
//                    Robot.getDrivetrain().runDistance(0.5, 6.75, _Drivetrain.Movements.cw);
//                }
//                else if (!Robot.getDrivetrain().isBusy()) {
//                    _state = State.Lift_linearslide;
//                    _justEntered = true;
//                }
//                break;
//            case Lift_linearslide:
//                if (_justEntered) {
//                    _justEntered = false;
//                    Robot.getLinearslide().runTime(-.2,3501);
//                }
//                else if (!Robot.getLinearslide().isBusy()) {
//                    _state = State.FORWARD;
//                    _justEntered = true;
//                }
//                break;
//            case FORWARD:
//                if (_justEntered) {
//                    _justEntered = false;
//                    Robot.getDrivetrain().runDistance(0.3, 15.75, _Drivetrain.Movements.forward);
//                }
//                else if (!Robot.getDrivetrain().isBusy()) {
//                    _state = State.RAISE_SLIDE;
//                    _justEntered = true;
//                }
//                break;
//            case move_forward2:
//                if (_justEntered) {
//                    _justEntered = false;
//                    Robot.getDrivetrain().runDistance(0.5, 3, _Drivetrain.Movements.forward);
//                }
//                else if (!Robot.getDrivetrain().isBusy()) {
//                    _state = State.Lowclaw;
//                    _justEntered = true;
//                }
//            case Lowclaw:
//                if(_justEntered){
//                    _justEntered=false;
//                    Robot.getClaw6().setPosition(0.3);
//                }
//                else if(!Robot.getClaw6().isBusy()){
//                    _state = State.Open_Claw1;
//                    _justEntered=true;
//                }
//                break;
//            case Open_Claw1:
//                if(_justEntered){
//                    _justEntered=false;
//                    Robot.getClaw().setPosition(0.7);
//                }
//                else if(!Robot.getClaw().isBusy()){
//                    _state = State.UpClaw;
//                    _justEntered=true;
//                }
//                break;
//            case UpClaw:
//                if(_justEntered){
//                    _justEntered=false;
//                    Robot.getClaw6().setPosition(0);
//                }
//                else if(!Robot.getClaw6().isBusy()){
//                    _state = State.Lowlinslide;
//                    _justEntered=true;
//                }
//                break;
//            case Lowlinslide:
//                if (_justEntered) {
//                    _justEntered = false;
//                    Robot.getLinearslide().runTime(.2,1016);
//                }
//                else if (!Robot.getLinearslide().isBusy()) {
//                    _state = State.Turn_left;
//                    _justEntered = true;
//                }
//                break;
//            case Turn_left:
//                if (_justEntered) {
//                    _justEntered = false;
//                    Robot.getDrivetrain().runDistance(0.5, 5, _Drivetrain.Movements.cw);
//                }
//                else if (!Robot.getDrivetrain().isBusy()) {
//                    _state = State.Move_back;
//                    _justEntered = true;
//                }
//                break;
//            case Move_back:
//                if (_justEntered) {
//                    _justEntered = false;
//                    Robot.getDrivetrain().runDistance(0.5, 36, _Drivetrain.Movements.backward);
//                }
//                else if (!Robot.getDrivetrain().isBusy()) {
//                    _state = State.park;
//                    _justEntered = true;
//                }
//                break;
//            case park:
//                if (_justEntered) {
//                    _justEntered = false;
//                    if (_parkingSpot==1) {//red
//                        Robot.getDrivetrain().runDistance(0.5, 7, _Drivetrain.Movements.left);
//
//                    } else if (_parkingSpot == 2) {//green
//                        Robot.getDrivetrain().runDistance(0.5, 1, _Drivetrain.Movements.forward);
//                    } else {
//                        Robot.getDrivetrain().runDistance(0.5, 7, _Drivetrain.Movements.right);
//                    }
//                }
//                if (_justEntered){
//                    _justEntered=false;
//                    if (parkingSpot.equals("red")){
//                        Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.left);
//                    }
//                    else if(parkingSpot.equals("green")){
//                        Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.right);
//                    }
//                    else {
//                        Robot.getDrivetrain().runDistance(0.5, 1, _Drivetrain.Movements.forward);
//                    }
//                }
//                break;
        }
    }

    private enum State {
        lilforward,
        Moveright,
        FORWARD_TO_LOW_POLE,
        Turnaxis,
        RaiseSlide,
        Turn_Claw,
        OPEN_CLAW,
        clawup,
        slidedown,
        turnback,
        moveback1,
        moveleft,
        MoveForward3,
        Move_Left,

        Move_Forward,
        Sense,
        back,
        LOWER,
        Wait,
        Close_claw,
        FORWARD,
        AngleClaw,
        move_forward1,
        Turn_Right,
        Lift_linearslide,
        move_forward2,
        Lowclaw,
        Open_Claw1,
        UpClaw,
        Lowlinslide,
        Turn_left,
        Move_back,
        park
    }
}