package com.mercandalli.tracker.device_specs

import com.google.gson.annotations.SerializedName

data class DeviceSpecs(
        @SerializedName("device-id")
        val deviceId: String,
        @SerializedName("device-manufacturer")
        val deviceManufacturer: String,
        @SerializedName("device-model")
        val deviceModel: String,
        @SerializedName("device-hardware")
        val deviceHardware: String,
        @SerializedName("device-os-version")
        val deviceOsVersion: Int)