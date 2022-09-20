package org.firstinspires.ftc.teamcode.Drivers;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Control.Robot;

import java.util.List;

public class _TFOD {

    private final TFObjectDetector _tfod;
    private List<Recognition> _recognitions;

    public _TFOD(VuforiaLocalizer vuforia, float confidenceThreshold, boolean isTF2, int inputSize, double zoom, double aspectRatio, String model, String[] labels) {
        int tfodMonitorViewId = Robot.hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", Robot.hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = confidenceThreshold;
        tfodParameters.isModelTensorFlow2 = isTF2;
        tfodParameters.inputSize = inputSize;
        _tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        _tfod.loadModelFromAsset(model, labels);

        //Activate TensorFlow Object Detection before we wait for the start command.
        //Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
        if (_tfod != null) {
            _tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            _tfod.setZoom(zoom, aspectRatio);
        }
    }

    public void activate() {
        _tfod.activate();
    }

    public void deactivate() {
        _tfod.deactivate();
    }

    public Recognition getRecognitionValidLabel(ValidRecognition validRecognition, String label) {
        for (int i = 0; i < _recognitions.size(); i++) {
            if (_recognitions.get(i) != null) {
                if (validRecognition.check(_recognitions.get(i)) && _recognitions.get(i).getLabel().equals(label)) {
                    return _recognitions.get(i);
                }
            }
        }
        return null;
    }

    public int countValidLabel(ValidRecognition validRecognition, String label) {
        int count = 0;
        for (Recognition recognition : _recognitions) {
            if (recognition != null) {
                if (validRecognition.check(recognition) && recognition.getLabel().equals(label)) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<Recognition> getLatestRecognitions() {
        List<Recognition> recognitions = _tfod.getUpdatedRecognitions();
        if (recognitions != null) {
            if (recognitions.size() > 0 || _recognitions == null) {
                _recognitions = recognitions;
            }
        }
        return _recognitions;
    }

    public List<Recognition> getRecognitions() {
        return _recognitions;
    }

    @FunctionalInterface
    public interface ValidRecognition {
        boolean check(Recognition recognition);
    }
}