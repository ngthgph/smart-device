import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class SmartDevice (val name: String, val category: String) {

    var deviceStatus = "online"
        protected set

    open val deviceType = "unknown"

    constructor(name: String, category: String, statusCode: Int) : this(name, category) {
        deviceStatus = when(statusCode) {
            0 -> "offline"
            1 -> "online"
            else -> "unknown"
        }
    }

    open fun turnOn() {
        deviceStatus = "On"
    }

    open fun turnOff() {
        deviceStatus = "Off"
    }
}

class SmartTvDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    private var speakVolume by RangeRegulator(2, 0, 100)

    private var channelNumber by RangeRegulator(1, 0, 100)

    override val deviceType = "Smart TV"

    override fun turnOn() {
        super.turnOn()
        println("$name is turned on. Speak volume is set to $speakVolume and channel number" +
                " is set to $channelNumber.")
    }

    override fun turnOff() {
        super.turnOff()
        println("$name is turned off")
    }

    fun increaseSpeakerVolume() {
        speakVolume++
        println("Speak volume increased to $speakVolume.")
    }

    fun nextChannel() {
        channelNumber++
        println("Channel number increase to $channelNumber.")
    }
}

class SmartLightDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    private var brightnessLevel by RangeRegulator(2, 0, 100)

    override val deviceType = "Smart Light"

    override fun turnOn() {
        super.turnOn()
        brightnessLevel = 2
        println("Smart Light turned on. The brightness level is $brightnessLevel.")
    }

    override fun turnOff() {
        super.turnOff()
        brightnessLevel = 0
        println("Smart Light turned off")
    }

    fun increaseBrightness() {
        brightnessLevel++
        println("Brightness increased to $brightnessLevel.")
    }
}

class SmartHome(
    val smartTvDevice: SmartTvDevice,
    val smartLightDevice: SmartLightDevice
) {

    var deviceTurnOnCount = 0
        private set

    fun turnOnTv() {
        deviceTurnOnCount++
        smartTvDevice.turnOn()
    }

    fun turnOffTv() {
        deviceTurnOnCount--
        smartTvDevice.turnOff()
    }

    fun increaseTvVolume() {
        smartTvDevice.increaseSpeakerVolume()
    }

    fun changeTvToNext() {
        smartTvDevice.nextChannel()
    }

    fun turnOnLight() {
        deviceTurnOnCount++
        smartLightDevice.turnOn()
    }

    fun turnOffLight() {
        deviceTurnOnCount--
        smartLightDevice.turnOff()
    }

    fun increaseLightBrightness() {
        smartLightDevice.increaseBrightness()
    }

    fun turnOffAllDevices() {
        turnOffTv()
        turnOffLight()
    }
}

class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int
): ReadWriteProperty<Any?, Int> {

    private var fieldData = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return fieldData
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}

fun main() {

    val smartHome = SmartHome(
        SmartTvDevice("Android TV", "Entertainment"),
        SmartLightDevice("Good Light", "Utility")
    )

    smartHome.turnOnTv()
    smartHome.turnOnLight()
    println("Total number of devices currently turned on: ${smartHome.deviceTurnOnCount}")
    println()

    smartHome.increaseTvVolume()
    smartHome.changeTvToNext()
    smartHome.increaseLightBrightness()
    println()

    smartHome.turnOffAllDevices()
    println("Total number of devices currently turned on: ${smartHome.deviceTurnOnCount}")
}