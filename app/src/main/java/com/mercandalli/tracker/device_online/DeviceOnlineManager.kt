package com.mercandalli.tracker.device_online

import com.mercandalli.tracker.device.Device

interface DeviceOnlineManager {

    fun initialize()

    //fun getDeviceSync(deviceTrackerId:String): Device

    fun getDevicesSync(): List<Device>

    fun registerDevicesListener(listener: OnDevicesListener)

    fun unregisterDevicesListener(listener: OnDevicesListener)

    interface OnDevicesListener {
        fun onDevicesChanged()
    }
}