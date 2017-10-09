package com.mercandalli.tracker.device_spec

interface DeviceSpecManager {

    fun getDeviceSpec(): DeviceSpec

    fun getCPUFrequencyCurrent(): IntArray
}
