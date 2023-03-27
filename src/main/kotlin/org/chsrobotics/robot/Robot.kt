package org.chsrobotics.robot

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler

class Robot : TimedRobot() {
    private lateinit var robotContainer: RobotContainer
    private var autonomousCommand: Command? = null

    override fun robotInit() {
        robotContainer = RobotContainer()
    }

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
    }

    override fun disabledInit() {}
    override fun disabledPeriodic() {}
    override fun disabledExit() {}
    override fun autonomousInit() {
        autonomousCommand = robotContainer.autonomousCommand
        autonomousCommand!!.schedule()
    }

    override fun autonomousPeriodic() {}
    override fun autonomousExit() {}
    override fun teleopInit() {
        if (autonomousCommand != null) {
            autonomousCommand!!.cancel()
        }
    }

    override fun teleopPeriodic() {}
    override fun teleopExit() {}
    override fun testInit() {
        CommandScheduler.getInstance().cancelAll()
    }

    override fun testPeriodic() {}
    override fun testExit() {}
}
