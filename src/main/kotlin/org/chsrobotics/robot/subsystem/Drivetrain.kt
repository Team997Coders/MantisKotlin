package org.chsrobotics.robot.subsystem

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.REVPhysicsSim
import edu.wpi.first.math.system.plant.DCMotor
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj2.command.SubsystemBase
import io.mehow.ruler.Distance
import org.chsrobotics.robot.Constant
import org.chsrobotics.robot.HardwareID
import org.chsrobotics.robot.Robot
import org.chsrobotics.robot.Tuning
import org.chsrobotics.robot.drivemode.DifferentialOutput

class Drivetrain : SubsystemBase() {
    enum class Gear(val shift: Boolean) {
        HIGH(!HardwareID.SHIFTER.inverted),
        LOW(HardwareID.SHIFTER.inverted)
    }
    enum class IdleMode(val value: CANSparkMax.IdleMode) {
        COAST(CANSparkMax.IdleMode.kCoast),
        BRAKE(CANSparkMax.IdleMode.kBrake)
    }

    // Motors
    private val leftFrontMotor = CANSparkMax(HardwareID.LEFT_FRONT_MOTOR.id, MotorType.kBrushless)
    private val leftBackMotor = CANSparkMax(HardwareID.LEFT_BACK_MOTOR.id, MotorType.kBrushless)
    private val rightFrontMotor = CANSparkMax(HardwareID.RIGHT_FRONT_MOTOR.id, MotorType.kBrushless)
    private val rightBackMotor = CANSparkMax(HardwareID.RIGHT_BACK_MOTOR.id, MotorType.kBrushless)

    // Encoders
    private val leftEncoder = Encoder(HardwareID.LEFT_ENCODER_A.id, HardwareID.LEFT_ENCODER_B.id)
    private val rightEncoder = Encoder(HardwareID.RIGHT_ENCODER_A.id, HardwareID.RIGHT_ENCODER_B.id)

    var gear: Gear
        get() = Gear.values().first { it.shift == Robot.pneumatics.shifter.get() }
        set(value) {
            if (value != gear) { Robot.pneumatics.shifter.set(value.shift) }
        }

    var idleMode = IdleMode.COAST
        set(mode) {
            if (mode != idleMode) {
                leftFrontMotor.idleMode = mode.value
                leftBackMotor.idleMode = mode.value
                rightFrontMotor.idleMode = mode.value
                rightBackMotor.idleMode = mode.value
                field = mode
            }
        }

    var leftOutput = 0.0
        set(speed) {
            leftFrontMotor.setVoltage(speed * Tuning.LEFT_MOTOR_MULTIPLIER * 12.0)
            leftBackMotor.setVoltage(speed * Tuning.LEFT_MOTOR_MULTIPLIER * 12.0)
            field = speed
        }

    var rightOutput = 0.0
        set(speed) {
            rightFrontMotor.setVoltage(speed * Tuning.RIGHT_MOTOR_MULTIPLIER * 12.0)
            rightBackMotor.setVoltage(speed * Tuning.RIGHT_MOTOR_MULTIPLIER * 12.0)
            field = speed
        }

    var output: DifferentialOutput
        get() = DifferentialOutput(leftOutput, rightOutput)
        set(value) {
            leftOutput = value.left
            rightOutput = value.right
        }

    var leftPosition: Distance = Distance.Zero
        private set
    var rightPosition: Distance = Distance.Zero
        private set

    var leftVelocity: Distance = Distance.Zero
        private set
    var rightVelocity: Distance = Distance.Zero
        private set

    init {
        // Add motors to simulation
        if (RobotBase.isSimulation()) {
            REVPhysicsSim.getInstance().addSparkMax(leftFrontMotor, DCMotor.getNEO(1))
            REVPhysicsSim.getInstance().addSparkMax(leftBackMotor, DCMotor.getNEO(1))
            REVPhysicsSim.getInstance().addSparkMax(rightFrontMotor, DCMotor.getNEO(1))
            REVPhysicsSim.getInstance().addSparkMax(rightBackMotor, DCMotor.getNEO(1))
        }
        // Invert motors if necessary
        leftFrontMotor.inverted = HardwareID.LEFT_FRONT_MOTOR.inverted
        leftBackMotor.inverted = HardwareID.LEFT_BACK_MOTOR.inverted
        rightFrontMotor.inverted = HardwareID.RIGHT_FRONT_MOTOR.inverted
        rightBackMotor.inverted = HardwareID.RIGHT_BACK_MOTOR.inverted

        // Set encoder ratios
        val distancePerPulse = Constant.ENCODER_RADIANS_PER_PULSE * Constant.WHEEL_RADIUS_METERS
        leftEncoder.distancePerPulse = distancePerPulse
        rightEncoder.distancePerPulse = distancePerPulse

        // Clear faults
        leftFrontMotor.clearFaults()
        leftBackMotor.clearFaults()
        rightFrontMotor.clearFaults()
        rightBackMotor.clearFaults()

        // Set default gear
        gear = Tuning.DEFAULT_GEAR
    }

    override fun periodic() {
        // Get new encoder positions and differentiate
        val leftPosition = Distance.ofMeters(leftEncoder.distance)
        val rightPosition = Distance.ofMeters(rightEncoder.distance)
        leftVelocity = (leftPosition - this.leftPosition) / Robot.period
        rightVelocity = (rightPosition - this.rightPosition) / Robot.period
        this.leftPosition = leftPosition
        this.rightPosition = rightPosition
    }
}
