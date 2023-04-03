package org.chsrobotics.robot.util

import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min

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

fun clamp(value: Double, min: Double, max: Double): Double =
    if (min>max) {max(max, min(min, value))} else {max(min, min(max, value))}

fun clamp(value: Double): Double = clamp(value, 0.0, 1.0)
