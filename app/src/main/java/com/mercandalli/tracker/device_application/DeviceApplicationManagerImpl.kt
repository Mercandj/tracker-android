package com.mercandalli.tracker.device_application

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import com.mercandalli.tracker.main_thread.MainThreadPost
import java.util.*
import kotlin.collections.ArrayList

internal class DeviceApplicationManagerImpl constructor(
        private val packageManager: PackageManager,
        private val activityManager: ActivityManager,
        private val usageStatsManager: UsageStatsManager,
        private val mainThreadPost: MainThreadPost,
        private val delegate: Delegate) : DeviceApplicationManager {

    private val deviceApplicationsListeners = ArrayList<DeviceApplicationManager.DeviceApplicationsListener>()
    private val deviceApplications = ArrayList<DeviceApplication>()

    init {
        refreshDeviceApplications()
    }

    override fun getDeviceApplications(): List<DeviceApplication> {
        return deviceApplications
    }

    override fun refreshDeviceApplications() {
        Thread(Runnable {
            val deviceApplicationsSync = getDeviceApplicationsSync()
            synchronized(deviceApplications) {
                deviceApplications.clear()
                deviceApplications.addAll(deviceApplicationsSync)
                notifyDeviceApplicationsListener()
            }
        }).start()
    }

    override fun needUsageStatsPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        return createUsageStats().isEmpty()
    }

    override fun requestUsagePermission() {
        delegate.requestUsagePermission()
    }

    override fun registerDeviceApplicationsListener(listener: DeviceApplicationManager.DeviceApplicationsListener) {
        if (deviceApplicationsListeners.contains(listener)) {
            return
        }
        deviceApplicationsListeners.add(listener)
    }

    override fun unregisterDeviceApplicationsListener(listener: DeviceApplicationManager.DeviceApplicationsListener) {
        deviceApplicationsListeners.remove(listener)
    }

    private fun getDeviceApplicationsSync(): List<DeviceApplication> {
        val sortingNativeFromUserApp = AppUtils.sortingNativeFromUserApp(packageManager, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val queryUsageStats: Map<String, UsageStats> = createUsageStats()

            val result = ArrayList<DeviceApplication>()
            for (deviceApplication in sortingNativeFromUserApp) {
                val usageStats = queryUsageStats[deviceApplication.`package`]
                val totalTimeInForeground = usageStats?.totalTimeInForeground ?: 0
                result.add(DeviceApplication(
                        deviceApplication.kindInstallation,
                        deviceApplication.androidAppName,
                        deviceApplication.`package`,
                        deviceApplication.versionCode,
                        deviceApplication.versionName,
                        deviceApplication.installedAt,
                        deviceApplication.updatedAt,
                        totalTimeInForeground,
                        deviceApplication.icon))
            }
            return result.sortedWith(compareBy({ it.totalTimeInForeground }, { it.installedAt })).reversed()
        }
        return sortingNativeFromUserApp.sortedWith(compareByDescending({ it.installedAt }))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createUsageStats(): Map<String, UsageStats> {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.MONTH, -1)
        val startTime = calendar.timeInMillis
        return usageStatsManager.queryAndAggregateUsageStats(
                startTime,
                endTime)
    }

    private fun notifyDeviceApplicationsListener() {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyDeviceApplicationsListener() })
            return
        }
        synchronized(deviceApplicationsListeners) {
            for (listener in deviceApplicationsListeners) {
                listener.onDeviceApplicationsChanged()
            }
        }
    }

    interface Delegate {
        fun requestUsagePermission()
    }
}