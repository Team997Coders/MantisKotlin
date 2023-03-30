package org.chsrobotics.robot.subsystem

import edu.wpi.first.wpilibj.PneumaticHub
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chsrobotics.robot.HardwareID
import org.chsrobotics.robot.Tuning

class Pneumatics : SubsystemBase() {
    private val hub = PneumaticHub()

    // Solenoids
    val shifter: Solenoid = hub.makeSolenoid(HardwareID.SHIFTER.id)
    val claw: Solenoid = hub.makeSolenoid(HardwareID.CLAW.id)

    init {
        hub.enableCompressorAnalog(Tuning.MIN_PRESSURE, Tuning.MAX_PRESSURE)
        hub.clearStickyFaults()
    }
}
