package com.mercandalli.tracker.device_specs

import android.os.Build

internal class DeviceSpecsManagerImpl : DeviceSpecsManager {

    private val deviceSpecs: DeviceSpecs

    init {
        val deviceOsVersion = Build.VERSION.SDK_INT
        this.deviceSpecs = DeviceSpecs(deviceOsVersion)
    }

    override fun getDeviceSpecs(): DeviceSpecs {
        return deviceSpecs
    }
}
