package org.chsrobotics.robot

import edu.wpi.first.wpilibj.RobotBase

fun main() {
    println("Starting robot!")
    RobotBase.startRobot {
        Robot
    }
}
