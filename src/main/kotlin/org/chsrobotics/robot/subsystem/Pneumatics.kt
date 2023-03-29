package org.chsrobotics.robot.subsystem

import edu.wpi.first.wpilibj.PneumaticHub
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chsrobotics.robot.Constant

class Pneumatics : SubsystemBase() {
    private val hub = PneumaticHub()

    // Solenoids
    val shifter: Solenoid = hub.makeSolenoid(Constant.Solenoids.SHIFTER.id)
    val claw: Solenoid = hub.makeSolenoid(Constant.Solenoids.CLAW.id)

    init {
        hub.enableCompressorAnalog(Constant.Pneumatics.MIN_PRESSURE, Constant.Pneumatics.MAX_PRESSURE)
        hub.clearStickyFaults()
    }
}
