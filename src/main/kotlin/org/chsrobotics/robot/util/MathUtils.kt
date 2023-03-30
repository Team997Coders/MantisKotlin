package org.chsrobotics.robot.util

import kotlin.math.PI

// Normalize radians
fun Double.normalizeRadians(): Double {
    return this % RADIAN
}

fun Double.toRadians(): Double {
    return Math.toRadians(this)
}

const val RADIAN = PI * 2

infix fun Double.distanceFrom(other: Double): Double {
    val diff = (this.normalizeRadians() - other.normalizeRadians())
    if (diff > PI) return diff - RADIAN
    if (diff < -PI) return diff + RADIAN
    return diff
}
