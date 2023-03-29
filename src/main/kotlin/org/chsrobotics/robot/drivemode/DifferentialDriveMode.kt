package org.chsrobotics.robot.drivemode

import edu.wpi.first.math.MathUtil
import kotlin.math.abs

/** Represents a mapping of joystick inputs to left and right drivetrain motor inputs.  */
interface DifferentialDriveMode {
    /**
     * Calculate the drive output using the current joystick inputs.
     *
     * @return The input, mapped to left and right side inputs.
     */
    fun execute(): DifferentialInput
}

/**
 * A data class that holds differential drivetrain inputs, split into drivetrain's left and right
 * sides.
 * @param left : value corresponding with left side input.
 * @param right : value corresponding with right side input.
 */
data class DifferentialInput (val left: Double, val right: Double) {
    /**
     * Returns a new DifferentialDrivetrainInput with the left and right values multiplied by a
     * scalar.
     *
     * @param scalar The multiplicand.
     * @return A scaled DifferentialDrivetrainInput.
     */
    fun multiply(scalar: Double): DifferentialInput {
        return DifferentialInput(left * scalar, right * scalar)
    }

    /**
     * Returns a new DifferentialDrivetrainInput consisting of the sum of left and right values.
     *
     * @param other The DifferentialDrivetrainInput to add.
     * @return The sum of the two DifferentialDrivetrainInputs.
     */
    fun add(other: DifferentialInput): DifferentialInput {
        return DifferentialInput(left + other.left, right + other.right)
    }

    /**
     * Returns a new DifferentialDrivetrainInput clamped to not exceed a maximum absolute value.
     *
     * @param maxAbsValue The absolute value to clamp to. If zero, this will return a
     * DifferentialDrivetrainInput of 0,0.
     * @return A DifferentialDrivetrainInput with left and right sides independently clamped.
     */
    fun clamp(maxAbsValue: Double): DifferentialInput {
        return if (abs(maxAbsValue) == 0.0) {
            DifferentialInput(0.0, 0.0)
        } else {
            DifferentialInput(
                MathUtil.clamp(left, -maxAbsValue, maxAbsValue),
                MathUtil.clamp(right, -maxAbsValue, maxAbsValue)
            )
        }
    }
}
