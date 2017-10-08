package com.mercandalli.tracker.device_spec

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.mercandalli.tracker.R

class DeviceSpecCard @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val deviceManufacturer: TextView
    private val deviceModel: TextView
    private val deviceOsVersion: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device_spec, this)
        deviceManufacturer = findViewById(R.id.view_device_spec_device_manufacturer)
        deviceModel = findViewById(R.id.view_device_spec_device_model)
        deviceOsVersion = findViewById(R.id.view_device_spec_device_os_version)
    }

    fun setDeviceSpecs(deviceSpec: DeviceSpec) {
        deviceManufacturer.text = deviceSpec.deviceManufacturer
        deviceModel.text = deviceSpec.deviceModel
        deviceOsVersion.text = deviceSpec.deviceOsVersion.toString()
    }
}