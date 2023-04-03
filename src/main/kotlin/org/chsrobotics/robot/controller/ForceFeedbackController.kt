package org.chsrobotics.robot.controller

import edu.wpi.first.networktables.NetworkTableInstance
import org.chsrobotics.robot.util.clamp

open class ForceFeedbackController {
    private val forceFeedbackTable = NetworkTableInstance.getDefault().getTable("ForceFeedback")
    init {
        forceFeedbackTable.getEntry("enabled").setBoolean(true)
    }

    fun stopAllForces() {
        stopConstantForce()
        stopDamperForce()
        stopSpringForce()
    }

    private val constantForceTable = forceFeedbackTable.getSubTable("ConstantForce")
    fun setConstantForce(magnitude: Double) {
        constantForceTable.getEntry("magnitude").setDouble(clamp(magnitude, -1.0, 1.0))
    }
    fun stopConstantForce() {
        setConstantForce(0.0)
    }

    private val damperForceTable = forceFeedbackTable.getSubTable("DamperForce")
    fun setDamperForce(negativeResistance: Double, positiveResistance: Double, constantForce: Double, deadBand: Double) {
        damperForceTable.getEntry("negativeResistance").setDouble(clamp(negativeResistance))
        damperForceTable.getEntry("positiveResistance").setDouble(clamp(positiveResistance))
        damperForceTable.getEntry("constantForce").setDouble(clamp(constantForce, -1.0, 1.0))
        damperForceTable.getEntry("deadBand").setDouble(clamp(deadBand))
    }
    fun stopDamperForce() {
        setDamperForce(0.0, 0.0, 0.0, 0.0)
    }

    private val springForceTable = forceFeedbackTable.getSubTable("SpringForce")
    fun setSpringForce(negativeSaturation: Double, positiveSaturation: Double, negativeGain: Double, positiveGain: Double, centerPoint: Double, deadBand: Double) {
        springForceTable.getEntry("negativeSaturation").setDouble(clamp(negativeSaturation))
        springForceTable.getEntry("positiveSaturation").setDouble(clamp(positiveSaturation))
        springForceTable.getEntry("negativeGain").setDouble(clamp(negativeGain))
        springForceTable.getEntry("positiveGain").setDouble(clamp(positiveGain))
        springForceTable.getEntry("centerPoint").setDouble(clamp(centerPoint, -1.0, 1.0))
        springForceTable.getEntry("deadBand").setDouble(clamp(deadBand))
    }
    fun stopSpringForce() {
        setSpringForce(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    }
}
