package org.chsrobotics.robot

object Constant {
    // Controller IDs
    object Controller {
        const val DRIVER = 0
        const val OPERATOR = 1
    }
    // Can IDs for motors
    object Motor {
        object Drivetrain {
            val LEFT_FRONT = Channel(2, true)
            val LEFT_BACK = Channel(3, true)
            val RIGHT_FRONT = Channel(4, false)
            val RIGHT_BACK = Channel(5, false)
        }
        object Arm {
            val DISTAL = Channel(6)
            val LEFT_LOCAL = Channel(7)
            val RIGHT_LOCAL = Channel(8)
        }
    }
    // Channels for encoders
    object Encoder {
        object Drivetrain {
            val LEFT_ENCODER_A = Channel(2)
            val LEFT_ENCODER_B = Channel(3)
            val RIGHT_ENCODER_A = Channel(0)
            val RIGHT_ENCODER_B = Channel(1)
        }
    }
    // Channels for solenoids
    object Solenoids {
        val SHIFTER = Channel(0, true)
        val CLAW = Channel(14, false)
    }
    // Other pneumatics constants
    object Pneumatics {
        val PRESSURE_SENSOR_CHANNEL = Channel(0)
        const val PRESSURE_WARNING_THRESHOLD = 60.0
        const val MIN_PRESSURE = 100.0
        const val MAX_PRESSURE = 120.0
    }

    object Drivetrain {
        val DEFAULT_GEAR = org.chsrobotics.robot.subsystem.Drivetrain.Gear.LOW
        const val DRIVE_LIMITER = 10.0
        const val TURN_LIMITER = 10.0
    }
}

data class Channel(val id: Int, val inverted: Boolean = false)
