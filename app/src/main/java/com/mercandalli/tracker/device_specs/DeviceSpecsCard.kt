package com.mercandalli.tracker.device_specs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.mercandalli.tracker.R

class DeviceSpecsCard @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val deviceManufacturer: TextView
    private val deviceModel: TextView
    private val deviceOsVersion: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device_specs, this)
        deviceManufacturer = findViewById(R.id.view_device_specs_device_manufacturer)
        deviceModel = findViewById(R.id.view_device_specs_device_model)
        deviceOsVersion = findViewById(R.id.view_device_specs_device_os_version)
    }

    fun setDeviceSpecs(deviceSpecs: DeviceSpecs) {
        deviceManufacturer.text = deviceSpecs.deviceManufacturer
        deviceModel.text = deviceSpecs.deviceModel
        deviceOsVersion.text = deviceSpecs.deviceOsVersion.toString()
    }
}