package com.mercandalli.tracker.device_online

import com.google.gson.Gson
import com.mercandalli.tracker.device_specs.DeviceSpecs

class DeviceSpecsConverter constructor(
        private val gson: Gson) {

    fun toByteArray(deviceSpecs: DeviceSpecs): ByteArray {
        val json = gson.toJson(deviceSpecs)
        return json.toByteArray()
    }
}