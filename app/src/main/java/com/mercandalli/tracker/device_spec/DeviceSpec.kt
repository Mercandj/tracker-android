package com.mercandalli.tracker.device_spec

import com.google.gson.annotations.SerializedName

data class DeviceSpec(
        @SerializedName("device-id")
        val deviceId: String,
        @SerializedName("device-manufacturer")
        val deviceManufacturer: String,
        @SerializedName("device-model")
        val deviceModel: String,
        @SerializedName("device-hardware")
        val deviceHardware: String,
        @SerializedName("device-os-version")
        val deviceOsVersion: Int,
        @SerializedName("device-density")
        val deviceDensity: String,
        @SerializedName("device-emulator")
        val deviceEmulator: Boolean,
        @SerializedName("device-rooted")
        val deviceRooted: Boolean,
        @SerializedName("device-battery-percent")
        val deviceBatteryPercent: Float) {

    data class Response(
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
            @SerializedName("device-density")
            var deviceDensity: String = "",
            @SerializedName("device-emulator")
            var deviceEmulator: Boolean = false,
            @SerializedName("device-rooted")
            var deviceRooted: Boolean = false,
            @SerializedName("device-battery-percent")
            var deviceBatteryPercent: Float = 0F) {

        fun toDeviceSpec(): DeviceSpec {
            return DeviceSpec(
                    deviceId,
                    deviceManufacturer,
                    deviceModel,
                    deviceHardware,
                    deviceOsVersion,
                    deviceDensity,
                    deviceEmulator,
                    deviceRooted,
                    deviceBatteryPercent
            )
        }
    }
}