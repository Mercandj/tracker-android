package com.mercandalli.tracker.device_specs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.mercandalli.tracker.R

class DeviceSpecsCard @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device, this)

    }

    fun setDeviceSpecs(deviceSpecs: DeviceSpecs) {

    }
}