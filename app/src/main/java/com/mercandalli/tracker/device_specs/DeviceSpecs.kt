package com.mercandalli.tracker.device_specs

data class DeviceSpecs internal constructor(
        val deviceId: String,
        val deviceManufacturer: String,
        val deviceModel: String,
        val deviceOsVersion: Int)