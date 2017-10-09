package com.mercandalli.tracker.device_spec

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
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
    private val deviceHardware: TextView
    private val deviceDensity: TextView
    private val deviceEmulator: TextView
    private val deviceRooted: TextView
    private val deviceBattery: TextView
    private val deviceMacAddress: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device_spec, this)
        deviceManufacturer = findViewById(R.id.view_device_spec_device_manufacturer)
        deviceModel = findViewById(R.id.view_device_spec_device_model)
        deviceOsVersion = findViewById(R.id.view_device_spec_device_os_version)
        deviceHardware = findViewById(R.id.view_device_spec_device_hardware)
        deviceDensity = findViewById(R.id.view_device_spec_device_density)
        deviceEmulator = findViewById(R.id.view_device_spec_device_emulator)
        deviceRooted = findViewById(R.id.view_device_spec_device_rooted)
        deviceBattery = findViewById(R.id.view_device_spec_device_battery)
        deviceMacAddress = findViewById(R.id.view_device_spec_device_mac_address)
    }

    fun setDeviceSpecs(deviceSpec: DeviceSpec) {
        setText(deviceManufacturer, "Manufacturer: ", deviceSpec.deviceManufacturer)
        setText(deviceModel, "Model: ", deviceSpec.deviceModel)
        setText(deviceOsVersion, "Android version: ", deviceSpec.deviceOsVersion.toString())
        setText(deviceHardware, "Hardware: ", deviceSpec.deviceHardware)
        setText(deviceDensity, "Density: ", deviceSpec.deviceDensity)
        setText(deviceEmulator, "Emulator: ", deviceSpec.deviceEmulator.toString())
        setText(deviceRooted, "Rooted: ", deviceSpec.deviceRooted.toString())
        setText(deviceBattery, "Battery: ", ((deviceSpec.deviceBatteryPercent * 100f).toInt()).toString() + "%")
        setText(deviceMacAddress, "Mac address: ", deviceSpec.deviceMacAddress.toString())
    }

    private fun setText(textView: TextView, bold: String, text: String) {
        val str = SpannableString(bold + text)
        str.setSpan(StyleSpan(Typeface.BOLD), 0, bold.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = str
    }
}