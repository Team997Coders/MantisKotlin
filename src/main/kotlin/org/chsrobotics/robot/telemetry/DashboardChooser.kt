package org.chsrobotics.robot.telemetry

import edu.wpi.first.networktables.NTSendable
import edu.wpi.first.networktables.NTSendableBuilder
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.util.sendable.SendableRegistry
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import java.security.InvalidParameterException
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

/**
 * A NTSendable which replaces the SendableChooser in favor of a listener-based api. Used for
 * constructing option widgets in the dashboard.
 *
 * @param <T> The data type of the option.
</T> */
class DashboardChooser<T> constructor(
    path: String,
    options: Map<String?, T>,
    defaultOption: String?,
) :
    NTSendable, AutoCloseable {
    private val channel: Int
    private val optionMap: Map<String?, T>
    private val defaultOption: String?
    private var selectedOption: String? = null
    private val listeners: MutableSet<(previous: T, next: T) -> Unit> = LinkedHashSet()
    private val activeEntries: MutableSet<NetworkTableEntry> = LinkedHashSet()
    private val mutex = ReentrantLock()

    /**
     * Interface implemented by enums which allows DashboardChooser to automatically create
     * choosers.
     */
    interface Option {
        val displayName: String
    }

    /**
     * Constructs a DashboardChooser from a `Map<String, T>`.
     *
     * @param options The option list, as a `Map<title, value>`.
     * @param defaultOption The title of the default option.
     * @param logChanges Whether to log changes in the chosen value to the default on-robot log at
     * `HighLevelLogger.getLog()`.
     */
    /**
     * Constructs a DashboardChooser from a `Map<String, T>`, recording changes to the chosen
     * value to the default on-robot log at `HighLevelLogger.getLog()`.
     *
     * @param options The option list, as a `Map<title, value>`.
     * @param defaultOption The title of the default option.
     */
    init {
        if (defaultOption != null && !options.containsKey(defaultOption)) {
            throw InvalidParameterException(
                "defaultOption must be either null or a valid option value"
            )
        }
        // set the channel to the nex available channel (each instance gets its own channel)
        channel = numInstances.getAndIncrement()

        optionMap = options
        this.defaultOption = defaultOption

        // register the chooser with NT
        SmartDashboard.putData(path, this)
    }

    override fun close() {
        SendableRegistry.remove(this)
    }

    val selected: T?
        /**
         * Gets the currently selected option's value.
         *
         * @return The option's value, of type T.
         */
        get() {
            mutex.lock()
            return try {
                if (selectedOption == null) {
                    optionMap[defaultOption]
                } else {
                    optionMap[selectedOption]
                }
            } finally {
                mutex.unlock()
            }
        }

    override fun initSendable(builder: NTSendableBuilder) {
        builder.setSmartDashboardType("String Chooser")
        // sets the channel
        builder.table.getEntry(".instance").setDouble(channel.toDouble())
        // set the default option
        builder.addStringProperty("default", { defaultOption }, null)
        // populate the options
        builder.addStringArrayProperty(
            "options",
            { optionMap.keys.toTypedArray() }, null
        )
        // define the function for shuffleboard to get the default option
        builder.addStringProperty(
            "active",
            {
                mutex.lock()
                try {
                    return@addStringProperty if (selectedOption == null) defaultOption else selectedOption
                } finally {
                    mutex.unlock()
                }
            },
            null
        )
        mutex.lock()
        try {
            activeEntries.add(builder.table.getEntry("active"))
        } finally {
            mutex.unlock()
        }
        // defines the function called with the value has been updated in shuffleboard
        builder.addStringProperty("selected", null) { option: String ->
            onOptionUpdate(
                option
            )
        }
    }

    private fun onOptionUpdate(option: String) {
        // get value before change
        val oldOption = selected
        mutex.lock()
        try {
            selectedOption = option
            for (entry in activeEntries) {
                entry.setString(option)
            }
        } finally {
            mutex.unlock()
        }
        // get value after
        val newOption = selected

        // call listeners
        for (listener in listeners) {
            listener(oldOption!!, newOption!!)
        }
    }

    /**
     * Register a listener to be called when the option is updated.
     *
     * @param updater The lambda to be called on update.
     */
    fun registerListener(updater: (previous: T, next: T) -> Unit) {
        listeners.add(updater)
    }

    /**
     * Unregister a listener when no longer needed.
     *
     * @param updater the lambda to remove.
     */
    fun unregisterListener(updater: (previous: T, next: T) -> Unit) {
        listeners.remove(updater)
    }

    companion object {
        private val numInstances = AtomicInteger()

        /**
         * Constructs a DashboardChooser from an enum implementing [Option].
         *
         * @param default The default enum option
         * @return A new DashboardChooser object.
         * @param <E> The enum's type.
        </T> */
        fun <E> fromEnum(
            path: String, default: E
        ): DashboardChooser<E> where E: Enum<E>, E: Option {
            val options: MutableMap<String?, E> = HashMap()
            default.declaringClass.enumConstants.forEach {
                options[it.displayName] = it
            }
            options[default.displayName] = default
            return DashboardChooser(path, options, default.displayName)
        }
    }
}