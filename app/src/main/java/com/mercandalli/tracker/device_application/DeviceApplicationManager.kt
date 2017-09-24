package com.mercandalli.tracker.device_application

interface DeviceApplicationManager {

    fun getDeviceApplications(): List<DeviceApplication>

    fun refreshDeviceApplications()

    fun needUsageStatsPermission(): Boolean

    fun requestUsagePermission()

    fun registerDeviceApplicationsListener(listener: DeviceApplicationsListener)

    fun unregisterDeviceApplicationsListener(listener: DeviceApplicationsListener)

    interface DeviceApplicationsListener {

        fun onDeviceApplicationsChanged()
    }
}