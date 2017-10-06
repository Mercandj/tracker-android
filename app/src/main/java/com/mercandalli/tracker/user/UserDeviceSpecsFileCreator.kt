package com.mercandalli.tracker.user

import com.google.gson.Gson
import com.mercandalli.tracker.device_specs.DeviceSpecs

class UserDeviceSpecsFileCreator constructor(
        private val gson: Gson) {

    fun toData(deviceSpecs: DeviceSpecs): ByteArray {
        val userDeviceSpecsRequest = UserDeviceSpecsRequest(
                deviceSpecs.deviceManufacturer,
                deviceSpecs.deviceModel,
                deviceSpecs.deviceOsVersion)
        val json = gson.toJson(userDeviceSpecsRequest)
        return json.toByteArray()
    }
}