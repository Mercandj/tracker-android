package com.mercandalli.tracker.device_application

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.mercandalli.tracker.R

class DeviceApplicationsPreviewCard @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val deviceApplicationsPreviewNumber: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device_applications_preview, this)
        deviceApplicationsPreviewNumber = findViewById(R.id.view_device_applications_preview_number)
    }

    fun setDeviceApplications(deviceApplications: List<DeviceApplication>) {
        deviceApplicationsPreviewNumber.text = deviceApplications.size.toString()
    }
}