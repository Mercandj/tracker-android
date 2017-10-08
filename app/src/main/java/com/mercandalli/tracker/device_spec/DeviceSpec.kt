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
        val deviceBatteryPercent: Float)