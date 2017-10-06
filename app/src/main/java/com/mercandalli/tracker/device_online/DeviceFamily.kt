package com.mercandalli.tracker.device_online

import com.google.gson.annotations.SerializedName

data class DeviceFamily(
        @SerializedName("device-family-id")
        val deviceFamilyId: String,
        @SerializedName("device-family-members")
        val deviceFamilyMembers: List<DeviceFamilyMember>)