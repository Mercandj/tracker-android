package com.mercandalli.tracker.device

import com.mercandalli.tracker.common.Preconditions
import com.mercandalli.tracker.device_application.DeviceApplicationManager
import com.mercandalli.tracker.main_thread.MainThreadPost

internal class DeviceRepositoryImpl constructor(
        private val device: Device,
        private val deviceApplicationManager: DeviceApplicationManager,
        private val mainThreadPost: MainThreadPost) : DeviceRepository {

    private val devices = HashMap<String, Device>()
    private val listeners = ArrayList<DeviceRepository.DevicesListener>()

    init {
        devices[device.deviceTrackerId] = Device.create(
                device,
                deviceApplicationManager.getDeviceApplications())
        deviceApplicationManager.registerDeviceApplicationsListener(createDeviceApplicationsListener())
    }

    override fun getDevices(): List<Device> {
        return ArrayList(devices.values)
    }

    override fun getDevice(): Device {
        return devices[this.device.deviceTrackerId] ?: throw IllegalStateException("Device null")
    }

    override fun getDevice(deviceTrackerId: String): Device? {
        return devices[deviceTrackerId]
    }

    override fun putDevices(devices: List<Device>) {
        for (device in devices) {
            Preconditions.checkNotNull(device)
            this.devices[device.deviceTrackerId] = device
        }
        notifyDevicesListeners()
    }

    override fun putDevice(device: Device?) {
        if (device == null) {
            return
        }
        devices[device.deviceTrackerId] = device
        notifyDevicesListeners()
    }

    override fun registerDevicesListener(
            listener: DeviceRepository.DevicesListener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterDevicesListener(
            listener: DeviceRepository.DevicesListener) {
        listeners.remove(listener)
    }

    private fun notifyDevicesListeners() {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyDevicesListeners() })
            return
        }
        synchronized(listeners) {
            for (listener in listeners) {
                listener.onDevicesChanged()
            }
        }
    }

    private fun updateDevice() {
        devices[device.deviceTrackerId] = Device.create(
                getDevice(),
                deviceApplicationManager.getDeviceApplications())
        notifyDevicesListeners()
    }

    private fun createDeviceApplicationsListener(): DeviceApplicationManager.DeviceApplicationsListener {
        return object : DeviceApplicationManager.DeviceApplicationsListener {
            override fun onDeviceApplicationsChanged() {
                updateDevice()
            }
        }
    }
}