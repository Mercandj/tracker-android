package com.mercandalli.tracker.device_application

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mercandalli.tracker.R

class DeviceApplicationCard @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val icon: ImageView
    private val title: TextView
    private val subtitle: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device_application_card, this)
        orientation = LinearLayout.VERTICAL
        icon = findViewById(R.id.view_application_card_icon)
        title = findViewById(R.id.view_application_card_title)
        subtitle = findViewById(R.id.view_application_card_subtitle)
    }

    internal fun setDeviceApplication(deviceApplications: DeviceApplication) {
        icon.setImageDrawable(deviceApplications.icon)
        title.text = deviceApplications.androidAppName
        subtitle.text = deviceApplications.nbLaunch.toString() + " launch"
    }
}