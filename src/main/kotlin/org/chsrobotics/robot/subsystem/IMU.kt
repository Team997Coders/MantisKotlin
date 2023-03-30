package org.chsrobotics.robot.subsystem

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chsrobotics.robot.Robot
import org.chsrobotics.robot.util.*

class IMU : SubsystemBase() {
    private val navX = if (RobotBase.isReal()) {AHRS()} else {null}
    enum class CalibrationState {
        UNCALIBRATED,
        CALIBRATING,
        CALIBRATED
    }
    var calibrationState = CalibrationState.UNCALIBRATED
        private set
        get() {
            return if (field == CalibrationState.UNCALIBRATED) {
                field
            } else {
                if (navX?.isCalibrating == true) {
                    CalibrationState.CALIBRATING
                } else {
                    CalibrationState.CALIBRATED
                }
            }
        }
    fun startCalibration() {
        calibrationState = CalibrationState.CALIBRATING
        navX?.calibrate()
    }

    // TODO add sim support
    val pitch
        get() = navX?.roll?.toRadiansFromNavX() ?: 0.0
    private var previousPitch = 0.0
    val yaw
        get() = navX?.yaw?.toRadiansFromNavX() ?: 0.0
    private var previousYaw = 0.0
    val roll
        get() = navX?.pitch?.toRadiansFromNavX() ?: 0.0
    private var previousRoll = 0.0

    var pitchRate = 0.0
        private set
    var yawRate = 0.0
        private set
    var rollRate = 0.0
        private set

    override fun periodic() {
        // update velocities
        pitchRate = (pitch distanceFrom previousPitch) / Robot.period
        yawRate = (yaw distanceFrom previousYaw) / Robot.period
        rollRate = (roll distanceFrom previousRoll) / Robot.period

        // update previous values
        previousPitch = pitch
        previousYaw = yaw
        previousRoll = roll
    }
}

// Convert NavX degrees to correctly oriented radians
private fun Float.toRadiansFromNavX(): Double {
    return (-this).toDouble().toRadians()
}
