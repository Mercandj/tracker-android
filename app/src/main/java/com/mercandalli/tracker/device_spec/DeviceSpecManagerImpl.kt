package com.mercandalli.tracker.device_spec

import com.mercandalli.tracker.common.Closer
import java.io.*
import java.util.regex.Pattern

internal class DeviceSpecManagerImpl constructor(
        private val deviceId: String,
        private val deviceDensity: String,
        private val deviceEmulator: Boolean,
        private val deviceRooted: Boolean,
        private val delegate: Delegate) : DeviceSpecManager {

    private val deviceSpec: DeviceSpec

    init {
        this.deviceSpec = createDeviceSpecsSync()
    }

    override fun getDeviceSpec(): DeviceSpec {
        return deviceSpec
    }

    private fun createDeviceSpecsSync(): DeviceSpec {
        val deviceManufacturer = delegate.getDeviceManufacturer()
        val deviceModel = delegate.getDeviceModel()
        val deviceHardware = delegate.getDeviceHardware()
        val deviceOsVersion = delegate.getDeviceOsVersion()
        val deviceBatteryPercent = delegate.getDeviceBatteryPercent()
        val deviceMacAddress = delegate.getDeviceMacAddress()
        val deviceTrackerId = deviceMacAddress ?: deviceId
        return DeviceSpec(
                deviceTrackerId,
                deviceId,
                deviceMacAddress,
                deviceManufacturer,
                deviceModel,
                deviceHardware,
                deviceOsVersion,
                deviceDensity,
                deviceEmulator,
                deviceRooted,
                deviceBatteryPercent)
    }

    override fun getCPUFrequencyCurrent(): IntArray {
        val output = IntArray(getNumCores())
        for (i in 0 until getNumCores()) {
            output[i] = readSystemFileAsInt("/sys/devices/system/cpu/cpu" + i.toString() + "/cpufreq/scaling_cur_freq")
        }
        return output
    }

    private fun getNumCores(): Int {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter : FileFilter {
            override fun accept(pathname: File): Boolean {
                //Check if filename is "cpu", followed by a single digit number
                return Pattern.matches("cpu[0-9]+", pathname.getName())
            }
        }

        return try {
            //Get directory containing CPU info
            val dir = File("/sys/devices/system/cpu/")
            //Filter to only list the devices we care about
            val files = dir.listFiles(CpuFilter())
            //Return the number of cores (virtual CPU devices)
            files.size
        } catch (e: Exception) {
            //Default to return 1 core
            1
        }
    }

    private fun readSystemFileAsInt(path: String): Int {
        val file = File(path)
        var br: BufferedReader? = null
        try {
            br = BufferedReader(FileReader(file))
            return Integer.parseInt(br.readLine())
        } catch (e: IOException) {

        } finally {
            Closer.closeSilently(br)
        }
        return 0
    }

    interface Delegate {

        fun getDeviceManufacturer(): String

        fun getDeviceModel(): String

        fun getDeviceHardware(): String

        fun getDeviceOsVersion(): Int

        fun getDeviceBatteryPercent(): Float

        fun getDeviceMacAddress(): String?
    }
}
