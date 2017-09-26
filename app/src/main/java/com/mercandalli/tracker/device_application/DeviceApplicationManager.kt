package com.mercandalli.tracker.device_application

interface DeviceApplicationManager {

    fun getDeviceApplications(): List<DeviceApplication>

    fun refreshDeviceApplications()

    fun needUsageStatsPermission(): Boolean

    fun requestUsagePermission()

    fun registerDeviceApplicationsListener(listener: DeviceApplicationsListener)

    fun unregisterDeviceApplicationsListener(listener: DeviceApplicationsListener)

    fun registerDeviceStatsPermissionListener(listener: DeviceStatsPermissionListener)

    fun unregisterDeviceStatsPermissionListener(listener: DeviceStatsPermissionListener)

    interface DeviceApplicationsListener {

        fun onDeviceApplicationsChanged()
    }

    interface DeviceStatsPermissionListener {

        fun onDeviceStatsPermissionChanged()
    }
}