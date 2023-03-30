package org.chsrobotics.robot.subsystem

import com.fazecast.jSerialComm.SerialPort
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.SubsystemBase
import kotlinx.coroutines.*
import org.chsrobotics.robot.coroutine.SpartanDispatcher
import java.io.IOException

class Led : SubsystemBase() {
    enum class State(val value: Int) {
        OFF(0x30), RED(0x31), BLUE(0x32), CONE(0x33), CUBE(0x34), MOVING(0x35)
    }

    private var connected = false
    private val serial: SerialPort? = try {
        val ports = SerialPort.getCommPorts().filter { it.systemPortName.startsWith("ttyACM") }
        // first port should be correct I'm going to cry if it's not
        if (ports.isEmpty()) {
            println("No arduino connected")
        }
        val serial = ports[0]
        println(serial.systemPortName)
        serial.openPort()
        serial.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0)
        serial
    } catch (e: Exception) {
        println("Failed to connect to LED Arduino!")
        println(e)
        null
    }

    fun setAllianceColors() {
        state = if (DriverStation.getAlliance() == DriverStation.Alliance.Red) {
            State.RED
        } else {
            State.BLUE
        }
    }

    var state = State.OFF
        set(value) {
            if (field != value) {
                field = value
                sendState(value)
            }
        }

    private fun sendState(state: State) {
        println("Logging new state: ${state.name}(0x${Integer.toHexString(state.value)})")
        if (connected) {
            // write thread
            CoroutineScope(SpartanDispatcher).launch {
                try {
                    serial!!.outputStream.write(state.value)
                } catch (e: IOException) {
                    println(e)
                }
            }
        }
    }

    override fun periodic() {
        if (serial != null) {
            CoroutineScope(SpartanDispatcher).launch {
                val avail = serial.bytesAvailable()
                if (avail > 0) {
                    if (!connected) {
                        connected = true
                        sendState(state)
                    }
                    val bytes = ByteArray(avail)
                    serial.readBytes(bytes, avail.toLong())
                    val received = String(bytes)
                    println("Arduino: $received")
                }
            }
        }
    }
}
