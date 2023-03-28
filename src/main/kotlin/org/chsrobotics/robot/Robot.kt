package org.chsrobotics.robot

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.Commands

class Robot : TimedRobot() {
    private var autonomousCommand: Command = Commands.print("No autonomous command configured")

    override fun robotInit() {
        Config.DRIVE_MODE_CHOOSER.registerListener { _, value ->
            println(value)
        }
    }

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
    }

    override fun disabledInit() {}
    override fun disabledPeriodic() {}
    override fun disabledExit() {}
    override fun autonomousInit() {
        autonomousCommand.schedule()
    }
    override fun autonomousPeriodic() {}
    override fun autonomousExit() {}
    override fun teleopInit() {
        autonomousCommand.cancel()
    }

    override fun teleopPeriodic() {}
    override fun teleopExit() {}
    override fun testInit() {
        CommandScheduler.getInstance().cancelAll()
    }

    override fun testPeriodic() {}
    override fun testExit() {}
}
