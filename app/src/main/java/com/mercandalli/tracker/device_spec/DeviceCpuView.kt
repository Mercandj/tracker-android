package com.mercandalli.tracker.device_spec

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main.TrackerComponent

class DeviceCpuView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), Runnable {

    private val appComponent: TrackerComponent = TrackerApplication.appComponent
    private val deviceSpecsManager = appComponent.provideDeviceSpecsManager()

    init {
        setBackgroundColor(Color.WHITE)
        gravity = Gravity.CENTER
        updateCpuText()
    }

    override fun run() {
        updateCpuText()
        postDelayed(this, 500)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        postDelayed(this, 500)
    }

    override fun onDetachedFromWindow() {
        removeCallbacks(this)
        super.onDetachedFromWindow()
    }

    @SuppressLint("SetTextI18n")
    private fun updateCpuText() {
        val cpuFrequencyCurrent = deviceSpecsManager.getCPUFrequencyCurrent()
        if (!cpuFrequencyCurrent.isEmpty()) {
            val str = StringBuilder("Freq\n")
            for (cpuFreq in cpuFrequencyCurrent) {
                str.append(cpuFreq / 1000).append(" MHz\n")
            }
            text = str.toString()
        }
    }
}