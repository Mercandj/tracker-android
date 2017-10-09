package com.mercandalli.tracker.device

import com.mercandalli.tracker.common.Preconditions
import com.mercandalli.tracker.device_application.DeviceApplication
import com.mercandalli.tracker.device_spec.DeviceSpec

data class Device(
        val deviceSpec: DeviceSpec,
        val deviceApplications: List<DeviceApplication>,
        val deviceToken: String?,
        val deviceNickname: String?,
        val deviceSlaves: List<String>) {

    companion object {
        fun create(device: Device, deviceApplications: List<DeviceApplication>): Device {
            Preconditions.checkNotNull(deviceApplications)
            return Device(
                    device.deviceSpec,
                    deviceApplications,
                    device.deviceToken,
                    device.deviceNickname,
                    device.deviceSlaves)
        }
    }

    val deviceTrackerId: String = deviceSpec.deviceTrackerId

    data class Response(
            var deviceSpec: DeviceSpec.Response = DeviceSpec.Response(),
            var deviceApplications: List<DeviceApplication.Response> = ArrayList(),
            var deviceToken: String = "",
            var deviceNickname: String = "",
            var deviceSlaves: List<String> = ArrayList()) {

        fun toDevice(): Device {
            val deviceApplicationsReal = ArrayList<DeviceApplication>()
            deviceApplications.mapTo(deviceApplicationsReal) { it.toDeviceApplication() }
            return Device(
                    deviceSpec.toDeviceSpec(),
                    deviceApplicationsReal,
                    deviceToken,
                    deviceNickname,
                    deviceSlaves
            )
        }
    }
}