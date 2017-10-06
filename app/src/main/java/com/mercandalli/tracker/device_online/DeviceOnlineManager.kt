package com.mercandalli.tracker.device_online

interface DeviceOnlineManager {

    fun send()

    fun getDeviceSpecs(deviceId: String)
}