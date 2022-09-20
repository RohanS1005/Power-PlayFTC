package org.firstinspires.ftc.teamcode.Drivers;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Control.Robot;

public class _IMU {

    private final String _NAME;
    private final double _OVERFLOW_THRESHOLD = 300;
    private final double _FULL_CIRCLE_DEG = 360;
    private final double _ELAPSED_TIME;
    private final boolean _yawOverflowProtection;

    private final BNO055IMU _imu;
    private Orientation _angles;
    private double _yaw = 0;
    private double _yawRaw = 0;
    private double _lastYawRaw = 0;
    private double _lastUpdateTime = 0;
    private boolean _willUpdate;
    private boolean _justStartedUpdating;
    private double _yawOffset;

    public _IMU(String name, double elapsedTime, boolean willUpdate, boolean yawOverflowProtection) {
        _NAME = name;
        _ELAPSED_TIME = elapsedTime;
        _yawOverflowProtection = yawOverflowProtection;
        willUpdate(willUpdate);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        _imu = Robot.hardwareMap.get(BNO055IMU.class, _NAME);
        _imu.initialize(parameters);
    }

    public _IMU(String name, boolean willUpdate, boolean yawOverflowProtection) {
        this(name, 100, willUpdate, yawOverflowProtection);
    }

    public void willUpdate(boolean willUpdate) {
        _willUpdate = willUpdate;
        _justStartedUpdating = _willUpdate;
        _lastUpdateTime = Robot.runtime.milliseconds() - _ELAPSED_TIME;
    }

    public void update() {
        if (_willUpdate && (Robot.runtime.milliseconds() >= _lastUpdateTime + _ELAPSED_TIME)) {
            _angles = _imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            //Check if overflow protection desired
            if (_yawOverflowProtection) {
                //Calculate raw yaw value
                _yawRaw = _angles.firstAngle;

                //Check if update was just turned on
                if (!_justStartedUpdating) {
                    //Check if delta raw readings is greater than threshold
                    if (_yawRaw - _lastYawRaw > _OVERFLOW_THRESHOLD) {
                        //Detect and revert overflow
                        _yawOffset -= _FULL_CIRCLE_DEG;
                    }
                    //Check if delta raw readings is less than negative threshold
                    else if (_yawRaw - _lastYawRaw < -_OVERFLOW_THRESHOLD) {
                        //Detect and revert overflow
                        _yawOffset += _FULL_CIRCLE_DEG;
                    }

                    //Add delta to current software sensor data
                    _yaw = _yawRaw + _yawOffset;
                }

                //Save reading as old reading
                _lastYawRaw = _yawRaw;

                //set _startTime to just now
                _lastUpdateTime = Robot.runtime.milliseconds();

                //Make sure just started updating is false
                _justStartedUpdating = false;
            }
        }
    }

    public String getName() {
        return _NAME;
    }

    public double getYaw() {
        if (_yawOverflowProtection) {
            return _yaw;
        }
        else {
            return _angles.firstAngle;
        }
    }

    public double getYaw360() {
        return _yaw % 360;
    }

    public double getRoll() {
        return _angles.secondAngle;
    }

    public double getPitch() {
        return _angles.thirdAngle;
    }

    //blue = true
    //red = false
    public void resetYaw() {
        _yawOffset = -_yawRaw;
    }

    public void resetYaw(double headingAngle){
        _yawOffset = -_yawRaw + headingAngle;
    }
}