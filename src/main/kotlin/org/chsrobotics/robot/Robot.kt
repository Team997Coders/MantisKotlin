package org.chsrobotics.robot

import com.revrobotics.REVPhysicsSim
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.Commands
import org.chsrobotics.robot.command.drivetrain.JoystickDrive
import org.chsrobotics.robot.controller.G920Controller
import org.chsrobotics.robot.subsystem.Drivetrain
import org.chsrobotics.robot.subsystem.Led
import org.chsrobotics.robot.subsystem.Pneumatics

object Robot : TimedRobot() {
    // Controllers
    val controller = G920Controller()

    // Subsystems
    val led = Led()
    val pneumatics = Pneumatics()
    val drivetrain = Drivetrain()

    // Commands
    private val joystickDrive = JoystickDrive()
    private var autonomousCommand = Commands.print("No autonomous command configured")

    // Util
    fun setAllianceLeds() {
        val alliance = DriverStation.getAlliance()
        if (alliance == Alliance.Red) {
            led.state = Led.State.RED
        } else {
            led.state = Led.State.BLUE
        }
    }

    override fun robotInit() {
        Config.DriveModeChooser.registerListener { _, value ->
            println(value)
        }
    }

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
    }

    override fun disabledInit() {

    }
    override fun disabledPeriodic() {
        // Set leds to current alliance color
        // Doing this in the periodic allows this to update when the alliance is switched in the driver station
        // Setting led state will only write to the Arduino if the new state differs from the current state.
        setAllianceLeds()
    }
    override fun disabledExit() {
        // Turn off leds
        led.state = Led.State.OFF
    }
    override fun autonomousInit() {
        autonomousCommand.schedule()
    }
    override fun autonomousPeriodic() {

    }
    override fun autonomousExit() {
        autonomousCommand.cancel()
    }
    override fun teleopInit() {
        joystickDrive.schedule()
    }

    override fun teleopPeriodic() {

    }
    override fun teleopExit() {
        joystickDrive.cancel()
    }
    override fun testInit() {
        CommandScheduler.getInstance().cancelAll()
    }

    override fun testPeriodic() {

    }
    override fun testExit() {

    }

    // Simulation
    override fun simulationInit() {
    }
    override fun simulationPeriodic() {
        REVPhysicsSim.getInstance().run()
    }
}
