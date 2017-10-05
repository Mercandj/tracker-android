package com.mercandalli.tracker.user

import com.google.gson.annotations.SerializedName

class UserDeviceSpecsRequest internal constructor(
        @SerializedName("device-manufacturer")
        val deviceManufacturer: String,
        @SerializedName("device-model")
        val deviceModel: String,
        @SerializedName("device-os-version")
        val deviceOsVersion: Int)