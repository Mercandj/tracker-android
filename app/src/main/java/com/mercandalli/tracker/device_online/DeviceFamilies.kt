package com.mercandalli.tracker.device_online

import com.google.gson.annotations.SerializedName

data class DeviceFamilies(
        @SerializedName("device-families")
        var deviceFamilies: Map<String, DeviceFamily>?)