package com.mercandalli.tracker.device_application

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
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
    private val permission: View
    private val refresh: View
    private val deviceStatsPermissionListener = createDeviceStatsPermissionListener()
    private val deviceApplicationManager = TrackerApplication.appComponent.provideDeviceApplicationManager()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device_applications_preview, this)
        deviceApplicationsPreviewNumber = findViewById(R.id.view_device_applications_preview_number)
        app1 = findViewById(R.id.view_device_applications_preview_app_1)
        app2 = findViewById(R.id.view_device_applications_preview_app_2)
        app3 = findViewById(R.id.view_device_applications_preview_app_3)
        app4 = findViewById(R.id.view_device_applications_preview_app_4)
        permission = findViewById(R.id.view_device_applications_preview_permission)
        refresh = findViewById(R.id.view_device_applications_preview_refresh)
        permission.setOnClickListener {
            deviceApplicationManager.requestUsagePermission()
        }
        refresh.setOnClickListener {
            deviceApplicationManager.refreshDeviceApplications()
        }
        findViewById<View>(R.id.view_device_applications_preview_more).setOnClickListener {
            DeviceApplicationsActivity.start(context)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        deviceApplicationManager.registerDeviceStatsPermissionListener(deviceStatsPermissionListener)
        syncPermissionVisibility()
    }

    override fun onDetachedFromWindow() {
        deviceApplicationManager.unregisterDeviceStatsPermissionListener(deviceStatsPermissionListener)
        super.onDetachedFromWindow()
    }

    internal fun setDeviceApplications(deviceApplications: List<DeviceApplication>) {
        setText(deviceApplicationsPreviewNumber, "Number of apps: ", deviceApplications.size.toString())

        if (deviceApplications.size < 4) {
            return
        }
        app1.setDeviceApplication(deviceApplications[0])
        app2.setDeviceApplication(deviceApplications[1])
        app3.setDeviceApplication(deviceApplications[2])
        app4.setDeviceApplication(deviceApplications[3])
    }

    private fun syncPermissionVisibility() {
        val needUsageStatsPermission = deviceApplicationManager.needUsageStatsPermission()
        permission.visibility = if (needUsageStatsPermission) View.VISIBLE else View.GONE
        refresh.visibility = if (needUsageStatsPermission) View.GONE else View.VISIBLE
    }

    private fun createDeviceStatsPermissionListener(): DeviceApplicationManager.DeviceStatsPermissionListener {
        return object : DeviceApplicationManager.DeviceStatsPermissionListener {
            override fun onDeviceStatsPermissionChanged() {
                syncPermissionVisibility()
            }
        }
    }

    private fun setText(textView: TextView, bold: String, text: String) {
        val str = SpannableString(bold + text)
        str.setSpan(StyleSpan(Typeface.BOLD), 0, bold.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = str
    }
}