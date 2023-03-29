package org.chsrobotics.robot

import org.chsrobotics.robot.telemetry.DashboardChooser

object Config {
    enum class DriveMode(override val displayName: String) : DashboardChooser.Option {
        ARCADE("Arcade"),
        CURVATURE("Curvature"),
        ARCADE_CURVATURE_MIX_EVEN("Arcade-Curvature Mix (Even Bias)"),
        ARCADE_CURVATURE_MIX_BIAS_CURVATURE("Arcade-Curvature Mix (Curvature Bias)"),
        ARCADE_CURVATURE_MIX_BIAS_ARCADE("Arcade-Curvature Mix (Arcade Bias)");
    }
    val DriveModeChooser = DashboardChooser.fromEnum("DriveModeChooser", DriveMode.ARCADE_CURVATURE_MIX_EVEN)
}
