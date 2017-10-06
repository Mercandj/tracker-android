package com.mercandalli.tracker.device_online

import com.google.gson.annotations.SerializedName

data class DeviceFamilyMember(
        @SerializedName("device-id")
        val deviceId: String,
        @SerializedName("gcm-token")
        val gcmToken: String)