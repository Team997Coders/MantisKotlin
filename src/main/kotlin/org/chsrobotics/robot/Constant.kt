package org.chsrobotics.robot

import org.chsrobotics.robot.util.*

data class Channel(val id: Int, val inverted: Boolean = false)

object HardwareID {
    // Controller IDs
    const val DRIVER_CONTROLLER = 0
    const val OPERATOR_CONTROLLER = 1
    const val WHEEL_CONTROLLER = 0

    // Can IDs for motors
    val LEFT_FRONT_MOTOR = Channel(2, true)
    val LEFT_BACK_MOTOR = Channel(3, true)
    val RIGHT_FRONT_MOTOR = Channel(4, false)
    val RIGHT_BACK_MOTOR = Channel(5, false)

    val DISTAL_MOTOR = Channel(6)
    val LEFT_LOCAL_MOTOR = Channel(7)
    val RIGHT_LOCAL_MOTOR = Channel(8)

    // Channels for encoders
    val LEFT_ENCODER_A = Channel(2)
    val LEFT_ENCODER_B = Channel(3)
    val RIGHT_ENCODER_A = Channel(0)
    val RIGHT_ENCODER_B = Channel(1)

    val SHIFTER = Channel(0, true)
    val CLAW = Channel(14, false)

    val PRESSURE_SENSOR_CHANNEL = Channel(0)
}

object Constant {
    // Circumference of 0.4787787204, gear ration of 3:1
    const val ENCODER_RADIANS_PER_PULSE = 1 / (3 * RADIAN)
    const val WHEEL_RADIUS_METERS = 0.0762
    const val TRACKWIDTH_METERS = 1

    object Drivetrain {

    }
}

object Tuning {
    // Pneumatics tuning
    const val PRESSURE_WARNING_THRESHOLD = 60.0
    const val MIN_PRESSURE = 100.0
    const val MAX_PRESSURE = 120.0

    // Drivetrain tuning
    val DEFAULT_GEAR = org.chsrobotics.robot.subsystem.Drivetrain.Gear.LOW
    const val DRIVE_LIMITER = 10.0
    const val TURN_LIMITER = 10.0
    const val LEFT_MOTOR_MULTIPLIER = 0.9
    const val RIGHT_MOTOR_MULTIPLIER = 1.0

    // Auto tuning
    const val BALANCE_SPEED = 0.3
    const val BALANCE_ANGLE = 0.05
    const val BALANCE_ANGLE_RATE = 0.2
}
