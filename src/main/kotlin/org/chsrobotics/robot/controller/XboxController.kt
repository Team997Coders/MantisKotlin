package org.chsrobotics.robot.controller

import edu.wpi.first.wpilibj.GenericHID
import org.chsrobotics.robot.Constant
import edu.wpi.first.wpilibj.XboxController as WPILibXboxController

class XboxController : Controller {
    private val driverController = WPILibXboxController(Constant.Controller.DRIVER)
    private val operatorController = WPILibXboxController(Constant.Controller.OPERATOR)
    override val linearAxis: Double
        get() = driverController.leftY
    override val rotationalAxis: Double
        get() = driverController.rightX
    override val slowModeAxis: Double
        get() = driverController.leftTriggerAxis
    override val shifterButton: Boolean
        get() = driverController.rightTriggerAxis >= 0.5
    override val coneButton: Boolean
        get() = operatorController.aButton
    override val cubeButton: Boolean
        get() = operatorController.bButton

    override fun setRumble() {
        driverController.setRumble(GenericHID.RumbleType.kBothRumble, 1.0)
    }
}
