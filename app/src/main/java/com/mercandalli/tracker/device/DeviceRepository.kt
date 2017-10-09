package com.mercandalli.tracker.device

interface DeviceRepository {

    fun getDevices(): List<Device>

    fun getDevice(): Device

    fun getDevice(deviceTrackerId: String): Device?

    fun putDevices(devices: List<Device>)

    fun putDevice(device: Device?)

    fun registerDevicesListener(listener: DevicesListener)

    fun unregisterDevicesListener(listener: DevicesListener)

    interface DevicesListener {

        fun onDevicesChanged()
    }
}