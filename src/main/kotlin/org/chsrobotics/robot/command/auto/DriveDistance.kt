package org.chsrobotics.robot.command.auto

import edu.wpi.first.wpilibj2.command.CommandBase
import io.mehow.ruler.Distance
import org.chsrobotics.robot.Robot
import org.chsrobotics.robot.drivemode.DifferentialInput

class DriveDistance(
    private val distance: Distance,
    private val speed: Double
) : CommandBase() {
    private lateinit var startDistance: Distance

    init {
        addRequirements(Robot.drivetrain)
    }

    override fun initialize() {
        startDistance = (Robot.drivetrain.leftPosition + Robot.drivetrain.rightPosition) / 2
    }
    override fun execute() {
        Robot.drivetrain.speed = DifferentialInput(speed, speed)
    }
    override fun isFinished() =
        ((Robot.drivetrain.leftPosition + Robot.drivetrain.rightPosition) / 2) - startDistance >= distance
}
