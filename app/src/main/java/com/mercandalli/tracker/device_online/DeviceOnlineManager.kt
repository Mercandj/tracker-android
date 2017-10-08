package com.mercandalli.tracker.device_online

import com.mercandalli.tracker.device_spec.DeviceSpec

interface DeviceOnlineManager {

    fun initialize()

    fun getDeviceSpecsSync(): List<DeviceSpec>

    fun registerDeviceSpecsListener(listener: OnDeviceSpecsListener)

    fun unregisterDeviceSpecsListener(listener: OnDeviceSpecsListener)

    interface OnDeviceSpecsListener {
        fun onDeviceSpecsChanged()
    }
}