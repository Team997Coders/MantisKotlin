package org.chsrobotics.robot.controller

import edu.wpi.first.wpilibj.GenericHID
import org.chsrobotics.robot.HardwareID

class G920Controller : ForceFeedbackController(), Controller {
    private val controller = GenericHID(HardwareID.WHEEL_CONTROLLER)
    val throttleAxis: Double
        get() = (-controller.getRawAxis(1)+1)/2
    val brakeAxis: Double
        get() = (-controller.getRawAxis(2)+1)/2
    val clutchAxis: Double
        get() = (-controller.getRawAxis(3)+1)/2
    val wheelAxis: Double
        get() = controller.getRawAxis(0)
    val reverseGearButton: Boolean
        get() = controller.getRawButton(12)
    val firstGearButton: Boolean
        get() = controller.getRawButton(13)
    val secondGearButton: Boolean
        get() = controller.getRawButton(14)
    override val linearAxis: Double
        get() = throttleAxis * if (reverseGearButton) {-1} else {if (firstGearButton || secondGearButton) {1} else {0}}
    override val rotationalAxis: Double
        get() = wheelAxis
    override val slowModeAxis: Double
        get() = clutchAxis
    override val brakeModeButton: Boolean
        get() = brakeAxis >= 25
    override val shifterButton: Boolean
        get() = secondGearButton
    override val coneButton: Boolean
        get() = controller.getRawButton(10)
    override val cubeButton: Boolean
        get() = controller.getRawButton(9)
}
