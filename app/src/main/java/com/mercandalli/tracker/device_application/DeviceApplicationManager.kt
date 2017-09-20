package com.mercandalli.tracker.device_application

interface DeviceApplicationManager {

    fun getDeviceApplications(): List<DeviceApplication>

    fun needUsageStatsPermission(): Boolean

    fun requestUsagePermission()
}