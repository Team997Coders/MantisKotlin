package org.chsrobotics.robot.drivemode

import edu.wpi.first.math.filter.SlewRateLimiter

/** Moves the robot in teleop using separate inputs for linear and rotational motion.
 *
 * @param linearAxis The axis to be used for linear movement.
 * @param rotationalAxis The axis to be used for rotational movement.
 * @param driveModifier A scalar to multiply the linear input by.
 * @param turnModifier A scalar to multiply the rotational input by.
 * @param driveLimiter The maximum rate of change of the linear input, in units of input /
 * second. If equal to 0, no rate limiting will be applied.
 * @param turnLimiter The maximum rate of change of the rotational input, in units of input /
 * second. If equal to 0, no rate limiting will be applied.
 */
class ArcadeDrive(
    private val linearAxis: () -> Double,
    private val rotationalAxis: () -> Double,
    private val driveModifier: Double,
    private val turnModifier: Double,
    driveLimiter: Double,
    turnLimiter: Double
) : DifferentialDriveMode {
    private val driveLimiter = SlewRateLimiter(driveLimiter)
    private val turnLimiter = SlewRateLimiter(turnLimiter)

    /** {@inheritDoc}  */
    override fun execute(): DifferentialOutput {
        val linear: Double = driveLimiter.calculate(linearAxis() * driveModifier)
        val rotation: Double = turnLimiter.calculate(rotationalAxis() * turnModifier)
        val left = linear + rotation
        val right = linear - rotation
        return DifferentialOutput(left, right).clamp(1.0)
    }
}
