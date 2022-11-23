package org.firstinspires.ftc.teamcode.Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._Drivetrain;

@Autonomous(group="Auton", preselectTeleOp = "FinalTeleOp")
public class RedLeft extends _Autonomous {

    private State _state;
    private boolean _justEntered;
    private int _parkingSpot;

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
                    Robot.getDrivetrain().runDistance(0.5, 5, _Drivetrain.Movements.forward);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.RAISE_SLIDE;
                    _justEntered = true;
                }
                break;
            case RAISE_SLIDE:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getLinearslide().runDistance(0.5, 2);
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
                    Robot.getClaw().setPosition(Robot.CLAW_OPEN);
                }
                else if(!Robot.getClaw().isBusy()){
                    _state = State.Move_Left;
                    _justEntered=true;
                }
                break;
            case Move_Left:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.left);
                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state = State.Move_Forward;
                    _justEntered=true;
                }
                break;
            case Move_Forward:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.forward);
                }
                else if(!Robot.getDrivetrain().isBusy()){
                    _state = State.Lowlinslide;
                    _justEntered=true;
                }
                break;
            case Low_Linearslide:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getLinearslide().runDistance(-0.5, 10);
                }
                else if (!Robot.getLinearslide().isBusy()) {
                    _state = State.Close_claw;
                    _justEntered = true;
                }
                break;
            case Close_claw:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getClaw().setPosition(Robot.CLAW_CLOSED);
                }
                else if (!Robot.getClaw().isBusy()) {
                    _state = State.move_forward1;
                    _justEntered = true;
                }
                break;
            case move_forward1:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.forward);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.Turn_Right;
                    _justEntered = true;
                }
                break;
            case Turn_Right:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.cw);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.AngleClaw;
                    _justEntered = true;
                }
                break;
            case AngleClaw:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw6().setPosition(0);
                }
                else if(!Robot.getClaw6().isBusy()){
                    _state = State.Lift_linearslide;
                    _justEntered=true;
                }
                break;
            case Lift_linearslide:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getLinearslide().runDistance(0.5, 10);
                }
                else if (!Robot.getLinearslide().isBusy()) {
                    _state = State.Lowclaw;
                    _justEntered = true;
                }
                break;
            case move_forward2:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getDrivetrain().runDistance(0.5, 3, _Drivetrain.Movements.forward);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.Lowclaw;
                    _justEntered = true;
                }
            case Lowclaw:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw6().setPosition(0.3);
                }
                else if(!Robot.getClaw6().isBusy()){
                    _state = State.Open_Claw1;
                    _justEntered=true;
                }
                break;
            case Open_Claw1:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw().setPosition(Robot.CLAW_OPEN);
                }
                else if(!Robot.getClaw().isBusy()){
                    _state = State.UpClaw;
                    _justEntered=true;
                }
                break;
            case UpClaw:
                if(_justEntered){
                    _justEntered=false;
                    Robot.getClaw6().setPosition(0);
                }
                else if(!Robot.getClaw6().isBusy()){
                    _state = State.Lowlinslide;
                    _justEntered=true;
                }
                break;
            case Lowlinslide:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getLinearslide().runDistance(-0.5, 10);
                }
                else if (!Robot.getLinearslide().isBusy()) {
                    _state = State.Turn_left;
                    _justEntered = true;
                }
                break;
            case Turn_left:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.cw);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.Move_back;
                    _justEntered = true;
                }
                break;
            case Move_back:
                if (_justEntered) {
                    _justEntered = false;
                    Robot.getDrivetrain().runDistance(0.5, 10, _Drivetrain.Movements.backward);
                }
                else if (!Robot.getDrivetrain().isBusy()) {
                    _state = State.park;
                    _justEntered = true;
                }
                break;
            case park:
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
                break;
        }
    }

    private enum State {
        FORWARD_TO_LOW_POLE,
        RAISE_SLIDE,
        Turn_Claw,
        OPEN_CLAW,
        Move_Left,
        Move_Forward,
        Low_Linearslide,
        Close_claw,
        move_forward1,
        Turn_Right,
        AngleClaw,
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
