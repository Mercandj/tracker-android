package com.mercandalli.tracker.device_application

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.mercandalli.tracker.R
import com.mercandalli.tracker.main.TrackerApplication

class DeviceApplicationsPreviewCard @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val deviceApplicationsPreviewNumber: TextView
    private val app1: DeviceApplicationCard
    private val app2: DeviceApplicationCard
    private val app3: DeviceApplicationCard
    private val app4: DeviceApplicationCard

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device_applications_preview, this)
        deviceApplicationsPreviewNumber = findViewById(R.id.view_device_applications_preview_number)
        app1 = findViewById(R.id.view_device_applications_preview_app_1)
        app2 = findViewById(R.id.view_device_applications_preview_app_2)
        app3 = findViewById(R.id.view_device_applications_preview_app_3)
        app4 = findViewById(R.id.view_device_applications_preview_app_4)
        findViewById<View>(R.id.view_device_applications_preview_refresh).setOnClickListener {
            TrackerApplication.appComponent.provideDeviceApplicationManager().refreshDeviceApplications()
        }
    }

    internal fun setDeviceApplications(deviceApplications: List<DeviceApplication>) {
        deviceApplicationsPreviewNumber.text = "Number of apps: " + deviceApplications.size.toString()
        if (deviceApplications.size < 4) {
            return
        }
        app1.setDeviceApplication(deviceApplications[0])
        app2.setDeviceApplication(deviceApplications[1])
        app3.setDeviceApplication(deviceApplications[2])
        app4.setDeviceApplication(deviceApplications[3])
    }
}