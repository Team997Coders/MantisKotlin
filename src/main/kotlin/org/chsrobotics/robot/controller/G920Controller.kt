package org.chsrobotics.robot.controller

import edu.wpi.first.wpilibj.GenericHID
import org.chsrobotics.robot.HardwareID

class G920Controller : ForceFeedbackController(), Controller {
    private val controller = GenericHID(HardwareID.WHEEL_CONTROLLER)
    override val linearAxis: Double
        get() = (-controller.getRawAxis(1)+1)/2
    val brakeAxis: Double
        get() = (-controller.getRawAxis(2)+1)/2
    override val rotationalAxis: Double
        get() = controller.getRawAxis(0)
    override val slowModeAxis: Double
        get() = controller.getRawAxis(3)
    override val brakeModeButton: Boolean
        get() = brakeAxis >= 25
    override val shifterButton: Boolean
        get() = controller.getRawButton(4)
    override val coneButton: Boolean
        get() = controller.getRawButton(9)
    override val cubeButton: Boolean
        get() = controller.getRawButton(8)
}
