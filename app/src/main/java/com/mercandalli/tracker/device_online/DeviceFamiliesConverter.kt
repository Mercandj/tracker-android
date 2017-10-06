package com.mercandalli.tracker.device_online

import com.google.gson.Gson

class DeviceFamiliesConverter constructor(
        private val gson: Gson) {

    fun toDeviceFamilies(json: String): DeviceFamilies {
        return gson.fromJson(json, DeviceFamilies::class.java)
    }

    fun toByteArray(deviceFamilies: DeviceFamilies): ByteArray {
        val json = gson.toJson(deviceFamilies)
        return json.toByteArray()
    }
}