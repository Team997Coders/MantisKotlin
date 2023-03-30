package org.chsrobotics.robot

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import io.mehow.ruler.Distance
import org.chsrobotics.robot.command.auto.Balance
import org.chsrobotics.robot.command.auto.DriveDistance

object Auto {
    val BALANCE = SequentialCommandGroup(
        DriveDistance(Distance.Companion.ofMeters(-0.3), 0.5),
        Balance()
    )
}
