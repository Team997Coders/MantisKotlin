package org.chsrobotics.robot.controller

import edu.wpi.first.wpilibj.GenericHID
import org.chsrobotics.robot.Constant

class G920Controller : Controller {
    private val controller = GenericHID(Constant.Controller.DRIVER)
    override val linearAxis: Double
        get() = (-controller.getRawAxis(1)+1)/2
    override val rotationalAxis: Double
        get() = controller.getRawAxis(0)
    override val slowModeAxis: Double
        get() = controller.getRawAxis(2)
    override val shifterButton: Boolean
        get() = controller.getRawButton(4)
    override val coneButton: Boolean
        get() = controller.getRawButton(9)
    override val cubeButton: Boolean
        get() = controller.getRawButton(8)

    override fun setRumble() {
        controller.setRumble(GenericHID.RumbleType.kBothRumble, 1.0)
    }
}
