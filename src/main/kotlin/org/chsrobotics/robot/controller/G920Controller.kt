package org.chsrobotics.robot.controller

import edu.wpi.first.wpilibj.GenericHID
import org.chsrobotics.robot.HardwareID

class G920Controller : Controller {
    private val controller = GenericHID(HardwareID.WHEEL_CONTROLLER)
    private val forceFeedback = ForceFeedbackController()
    override val linearAxis: Double
        get() = (-controller.getRawAxis(1)+1)/2
    override val rotationalAxis: Double
        get() = controller.getRawAxis(0)
    override val slowModeAxis: Double
        get() = controller.getRawAxis(3)
    override val brakeModeButton: Boolean
        get() = controller.getRawAxis(2) >= 25
    override val shifterButton: Boolean
        get() = controller.getRawButton(4)
    override val coneButton: Boolean
        get() = controller.getRawButton(9)
    override val cubeButton: Boolean
        get() = controller.getRawButton(8)

    override fun setConstantForce(magnitude: Double) {
        forceFeedback.playConstantForce(magnitude)
    }
}
