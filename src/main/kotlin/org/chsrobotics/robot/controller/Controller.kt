package org.chsrobotics.robot.controller

interface Controller {
    val linearAxis: Double
    val rotationalAxis: Double
    val slowModeAxis: Double
    val brakeModeButton: Boolean
    val shifterButton: Boolean
    val coneButton: Boolean
    val cubeButton: Boolean
    fun setRumble() {}
}
