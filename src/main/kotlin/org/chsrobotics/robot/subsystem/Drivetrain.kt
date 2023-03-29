package org.chsrobotics.robot.subsystem

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.REVPhysicsSim
import edu.wpi.first.math.system.plant.DCMotor
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chsrobotics.robot.Constant
import org.chsrobotics.robot.Robot
import org.chsrobotics.robot.drivemode.DifferentialInput

class Drivetrain : SubsystemBase() {
    enum class Gear(val shift: Boolean) {
        HIGH(!Constant.Solenoids.SHIFTER.inverted),
        LOW(Constant.Solenoids.SHIFTER.inverted)
    }
    enum class IdleMode(val value: CANSparkMax.IdleMode) {
        COAST(CANSparkMax.IdleMode.kCoast),
        BRAKE(CANSparkMax.IdleMode.kBrake)
    }

    // Motors
    private val leftFrontMotor = CANSparkMax(Constant.Motor.Drivetrain.LEFT_FRONT.id, MotorType.kBrushless)
    private val leftBackMotor = CANSparkMax(Constant.Motor.Drivetrain.LEFT_BACK.id, MotorType.kBrushless)
    private val rightFrontMotor = CANSparkMax(Constant.Motor.Drivetrain.RIGHT_FRONT.id, MotorType.kBrushless)
    private val rightBackMotor = CANSparkMax(Constant.Motor.Drivetrain.RIGHT_BACK.id, MotorType.kBrushless)

    // Encoders
    private val leftEncoder = Encoder(Constant.Encoder.Drivetrain.LEFT_ENCODER_A.id, Constant.Encoder.Drivetrain.LEFT_ENCODER_B.id)
    private val rightEncoder = Encoder(Constant.Encoder.Drivetrain.RIGHT_ENCODER_A.id, Constant.Encoder.Drivetrain.RIGHT_ENCODER_B.id)

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

    var leftVoltage = 0.0
        set(speed) {
            leftFrontMotor.setVoltage(speed)
            leftBackMotor.setVoltage(speed)
            field = speed
        }

    var rightVoltage = 0.0
        set(speed) {
            rightFrontMotor.setVoltage(speed)
            rightBackMotor.setVoltage(speed)
            field = speed
        }

    var voltage: DifferentialInput
        get() = DifferentialInput(leftVoltage, rightVoltage)
        set(value) {
            leftVoltage = value.left
            rightVoltage = value.right
        }

    override fun periodic() {
    }

    init {
        // Add motors to simulation
        if (RobotBase.isSimulation()) {
            REVPhysicsSim.getInstance().addSparkMax(leftFrontMotor, DCMotor.getNEO(1))
            REVPhysicsSim.getInstance().addSparkMax(leftBackMotor, DCMotor.getNEO(1))
            REVPhysicsSim.getInstance().addSparkMax(rightFrontMotor, DCMotor.getNEO(1))
            REVPhysicsSim.getInstance().addSparkMax(rightBackMotor, DCMotor.getNEO(1))
        }
        // Invert motors if necessary
        leftFrontMotor.inverted = Constant.Motor.Drivetrain.LEFT_FRONT.inverted
        leftBackMotor.inverted = Constant.Motor.Drivetrain.LEFT_BACK.inverted
        rightFrontMotor.inverted = Constant.Motor.Drivetrain.RIGHT_FRONT.inverted
        rightBackMotor.inverted = Constant.Motor.Drivetrain.RIGHT_BACK.inverted

        // Clear faults
        leftFrontMotor.clearFaults()
        leftBackMotor.clearFaults()
        rightFrontMotor.clearFaults()
        rightBackMotor.clearFaults()

        // Set default gear
        gear = Constant.Drivetrain.DEFAULT_GEAR
    }
}