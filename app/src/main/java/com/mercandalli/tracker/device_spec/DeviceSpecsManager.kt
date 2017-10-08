package com.mercandalli.tracker.device_spec

interface DeviceSpecsManager {

    fun getDeviceSpec(): DeviceSpec

    fun getCPUFrequencyCurrent(): IntArray
}
