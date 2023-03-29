package org.chsrobotics.robot.command.drivetrain

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj2.command.CommandBase
import org.chsrobotics.robot.Constant
import org.chsrobotics.robot.Robot
import org.chsrobotics.robot.drivemode.ArcadeDrive
import org.chsrobotics.robot.drivemode.DifferentialInput

class JoystickDrive : CommandBase() {
    private var driveMode = ArcadeDrive(
        {Robot.controller.linearAxis},
        {Robot.controller.rotationalAxis},
        1.0, 1.0,
        Constant.Drivetrain.DRIVE_LIMITER,
        Constant.Drivetrain.TURN_LIMITER
    )

    override fun execute() {
        Robot.controller.setRumble()
//        // Set idle mode
//        Robot.drivetrain.idleMode = if (Robot.driverController.rightBumper) {
//            Drivetrain.IdleMode.BRAKE
//        } else {
//            Drivetrain.IdleMode.COAST
//        }
//
//        // Set shifters
//        Robot.drivetrain.gear = if (Robot.driverController.rightTriggerAxis >= 0.5) {
//            Drivetrain.Gear.HIGH
//        } else {
//            Drivetrain.Gear.LOW
//        }
//
//        // Set leds
//        Robot.led.state = if (Robot.operatorController.aButton) {
//            Led.State.CONE
//        } else if (Robot.operatorController.bButton) {
//            Led.State.CUBE
//        } else {
//            Led.State.OFF
//        }

        // Set drivetrain motor outputs
//        Robot.drivetrain.voltage = DifferentialInput(0.5, 0.5).multiply(12.0)
        Robot.drivetrain.voltage = driveMode.execute().multiply(12.0)
    }

    override fun end(interrupted: Boolean) {
        Robot.drivetrain.voltage = DifferentialInput(0.0, 0.0)
    }
}
