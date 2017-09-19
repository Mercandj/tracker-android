package com.mercandalli.tracker.device_specs

import android.os.Build

internal class DeviceSpecsManagerImpl : DeviceSpecsManager {

    private val deviceSpecs: DeviceSpecs

    init {
        this.deviceSpecs = createDeviceSpecsSync()
    }

    override fun getDeviceSpecs(): DeviceSpecs {
        return deviceSpecs
    }

    private fun createDeviceSpecsSync(): DeviceSpecs {
        val deviceManufacturer = Build.MANUFACTURER
        val deviceModel = Build.MODEL
        val deviceOsVersion = Build.VERSION.SDK_INT
        return DeviceSpecs(
                deviceManufacturer,
                deviceModel,
                deviceOsVersion)
    }
}
