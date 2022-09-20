package org.firstinspires.ftc.teamcode.DriverTests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._Autonomous;
import org.firstinspires.ftc.teamcode.Drivers._TFOD;
import org.firstinspires.ftc.teamcode.Drivers._Vuforia;

@Autonomous(name="TFODTest", group="DriverTest")
public class TFODTest extends _Autonomous {

    private _TFOD _tfod;
    private _TFOD.ValidRecognition _validRecognition;

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry);
        _Vuforia vuforia = new _Vuforia("Webcam 1");
        _tfod = new _TFOD(vuforia.getVuforia(), 0.45f, true, 320, 1.1, 16.0/9.0,
                "FreightFrenzy_BCDM.tflite", new String[] {"Ball", "Cube", "Duck", "Marker"});
        _validRecognition = recognition -> ((recognition.getWidth() * recognition.getHeight()) < (320.0 * 640.0 / 8.0)) && (recognition.getWidth()/recognition.getHeight()) > 1.0;
    }

    @Override
    public void init_loop() {
        telemetry.addLine(_tfod.getLatestRecognitions() != null ? _tfod.getRecognitions().toString() : "NULL");

        Recognition temp = _tfod.getRecognitionValidLabel(_validRecognition, "Marker");
        telemetry.addLine(String.valueOf(_tfod.countValidLabel(_validRecognition, "Marker")));
        telemetry.addLine(temp != null ? temp.toString() : "NULL");
    }

    @Override
    public void loop() {}
}