package org.chsrobotics.robot.command.drivetrain

import edu.wpi.first.wpilibj2.command.CommandBase
import org.chsrobotics.robot.Robot
import org.chsrobotics.robot.Tuning
import org.chsrobotics.robot.drivemode.ArcadeDrive
import org.chsrobotics.robot.drivemode.DifferentialOutput
import org.chsrobotics.robot.subsystem.Drivetrain
import org.chsrobotics.robot.subsystem.Led

class JoystickDrive : CommandBase() {
    private var driveMode = ArcadeDrive(
        {Robot.controller.linearAxis},
        {Robot.controller.rotationalAxis},
        1.0, 1.0,
        Tuning.DRIVE_LIMITER,
        Tuning.TURN_LIMITER
    )

    init {
        addRequirements(Robot.drivetrain)
    }

    override fun execute() {
        // Set idle mode
        Robot.drivetrain.idleMode = if (Robot.controller.brakeModeButton) {
            Drivetrain.IdleMode.BRAKE
        } else {
            Drivetrain.IdleMode.COAST
        }

        // Set shifters
        Robot.drivetrain.gear = if (Robot.controller.shifterButton) {
            Drivetrain.Gear.HIGH
        } else {
            Drivetrain.Gear.LOW
        }

        // Set leds
        Robot.led.state = if (Robot.controller.coneButton) {
            Led.State.CONE
        } else if (Robot.controller.cubeButton) {
            Led.State.CUBE
        } else {
            Led.State.OFF
        }

        // Set Force Feedback
        var avgSpeed = ((Robot.drivetrain.leftVelocity+Robot.drivetrain.rightVelocity)/2).meters.toDouble()
        Robot.controller.setConstantForce(Robot.controller.rotationalAxis * avgSpeed * 0.25)

        // Set drivetrain motor outputs
        Robot.drivetrain.output = driveMode.execute()
    }

    override fun end(interrupted: Boolean) {
        // Disable Force Feedback
        Robot.controller.setConstantForce(0.0)

        Robot.drivetrain.output = DifferentialOutput(0.0, 0.0)
    }
}
