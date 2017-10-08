package com.mercandalli.tracker.device_online

import com.google.gson.annotations.SerializedName
import com.mercandalli.tracker.device_spec.DeviceSpec

data class DeviceSpecResponse(
        @SerializedName("device-id")
        var deviceId: String = "",
        @SerializedName("device-manufacturer")
        var deviceManufacturer: String = "",
        @SerializedName("device-model")
        var deviceModel: String = "",
        @SerializedName("device-hardware")
        var deviceHardware: String = "",
        @SerializedName("device-os-version")
        var deviceOsVersion: Int = 0,
        @SerializedName("device-emulator")
        var deviceEmulator: Boolean = false,
        @SerializedName("device-rooted")
        var deviceRooted: Boolean = false) {

    fun toDeviceSpecs(): DeviceSpec {
        return DeviceSpec(
                deviceId,
                deviceManufacturer,
                deviceModel,
                deviceHardware,
                deviceOsVersion,
                deviceEmulator,
                deviceRooted
        )
    }
}