package org.chsrobotics.robot.controller

import edu.wpi.first.wpilibj.GenericHID
import org.chsrobotics.robot.HardwareID
import edu.wpi.first.wpilibj.XboxController as WPILibXboxController

class XboxController : Controller {
    private val driverController = WPILibXboxController(HardwareID.DRIVER_CONTROLLER)
    private val operatorController = WPILibXboxController(HardwareID.OPERATOR_CONTROLLER)
    override val linearAxis: Double
        get() = driverController.leftY
    override val rotationalAxis: Double
        get() = driverController.rightX
    override val slowModeAxis: Double
        get() = driverController.leftTriggerAxis
    override val brakeModeButton: Boolean
        get() = driverController.rightBumper
    override val shifterButton: Boolean
        get() = driverController.rightTriggerAxis >= 0.5
    override val coneButton: Boolean
        get() = operatorController.aButton
    override val cubeButton: Boolean
        get() = operatorController.bButton

    override fun setRumble() {
        driverController.setRumble(GenericHID.RumbleType.kBothRumble, 1.0)
        operatorController.setRumble(GenericHID.RumbleType.kBothRumble, 1.0)
    }
}
