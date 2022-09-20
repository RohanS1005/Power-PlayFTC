package org.firstinspires.ftc.teamcode.Drivers;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Control.Robot;

public class _Vuforia {

    private final String _WEBCAM_NAME;
    private static final String _VUFORIA_KEY = "ASYtBET/////AAABmSTQiLUzLEx3qLnHm6hu7Y1aNDWPDgMBKY8lFonYrzU8M5f9mAV5KiaJ9YZWCSgoUx6/AKuobb1cLgB8R+mDHgx6FoP3XS3K8bAwShz98sojuAKmTGzJMZVUjH8mjW+9ebYjtw3oZr/ZM2F2NZuCPN4Rx+K5koMfR2IE1OQKoZbkgLJSc36yUmis7MN91L0xIgntCKhqpZkRX45VjWsZi4BcKQnK5L2YfUqueZ7qvPzpF7sWDDcWYqkLZNbxfRk+gUVdabq/uOPYR8v0O0EFONv7h2kiU3E1s7Rm8WOukfwfqa5Nsw7FSNF2kjL0PhPbGPBQ6kVbLQMsvmxM7x/AA2owHe8l1yHgzyCgd7YTFOdi";

    private final VuforiaLocalizer _vuforia;

    public _Vuforia(String webcamName) {
        _WEBCAM_NAME = webcamName;
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = _VUFORIA_KEY;
        parameters.cameraName = Robot.hardwareMap.get(WebcamName.class, _WEBCAM_NAME);
        _vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    public VuforiaLocalizer getVuforia() {
        return _vuforia;
    }
}