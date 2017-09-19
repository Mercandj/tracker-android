package com.mercandalli.tracker.device_application

import android.content.pm.PackageManager

internal class DeviceApplicationManagerImpl constructor(
        private val packageManager: PackageManager) : DeviceApplicationManager {

    override fun getDeviceApplications(): List<DeviceApplication> {
        return AppUtils.sortingNativeFromUserApp(packageManager, true)
    }
}