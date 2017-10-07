package com.mercandalli.tracker.device_specs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.mercandalli.tracker.common.Preconditions

object DeviceSpecsUtils {

    /**
     * https://stackoverflow.com/questions/2799097/how-can-i-detect-when-an-android-application-is-running-in-the-emulator
     */
    internal fun isEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
    }

    /**
     * Attention not unique on some devices, chinese... =(.
     *
     *
     * Returns a unique device using [android.provider.Settings.Secure.ANDROID_ID].
     * This id is randomly generated when the user first sets up the device and remains constant
     * for the lifetime of the user's device.
     *
     *
     * Check : http://developer.android.com/reference/android/provider/Settings.Secure.html#ANDROID_ID
     *
     * @return : The device id which is a 64-bit number as a hex string
     */
    @SuppressLint("HardwareIds")
    internal fun getDeviceId(context: Context): String {
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        Preconditions.checkNotNull(deviceId)
        return deviceId
    }
}