package org.chsrobotics.robot.controller

import edu.wpi.first.networktables.NetworkTableInstance
import kotlin.math.*

class ForceFeedbackController {
    private val table = NetworkTableInstance.getDefault().getTable("ForceFeedback")
    init {
        table.getEntry("enabled").setBoolean(true)
    }
    fun playConstantForce(magnitude: Double) {
        table.getEntry("magnitude").setDouble(max(-1.0, min(1.0, magnitude)))
    }
}
