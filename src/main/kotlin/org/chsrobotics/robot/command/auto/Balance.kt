package org.chsrobotics.robot.command.auto

import edu.wpi.first.wpilibj2.command.CommandBase
import org.chsrobotics.robot.Robot
import org.chsrobotics.robot.Tuning
import org.chsrobotics.robot.drivemode.DifferentialInput
import org.chsrobotics.robot.subsystem.Drivetrain
import org.chsrobotics.robot.subsystem.Led
import kotlin.math.*

class Balance : CommandBase() {
    init {
        addRequirements(Robot.drivetrain)
    }

    override fun execute() {
        val pitch = Robot.imu.pitch
        // Gets the rate of change of pitch towards balanced state, 0 if it is going away from balanced state
        // (lets us not enable brake mode when initially climbing the charge station).
        val pitchRate = if (pitch>0) {min(0.0, Robot.imu.pitchRate)} else {max(0.0, Robot.imu.pitchRate)}

        if (abs(Robot.imu.pitch) < Tuning.BALANCE_ANGLE || abs(pitchRate) > Tuning.BALANCE_ANGLE_RATE) {
            // We're balanced, so enable brake mode
            Robot.drivetrain.idleMode = Drivetrain.IdleMode.BRAKE

            // Important stuff
            Robot.led.setAllianceColors()
        } else {
            // Drive uphill
            val speed = if (pitch>0) {-Tuning.BALANCE_SPEED} else {Tuning.BALANCE_SPEED}
            Robot.drivetrain.speed = DifferentialInput(speed, speed)

            // Keep leds off unless balanced
            Robot.led.state = Led.State.OFF
        }
    }
    override fun end(interrupted: Boolean) {
        Robot.drivetrain.idleMode = Drivetrain.IdleMode.BRAKE
    }
}
