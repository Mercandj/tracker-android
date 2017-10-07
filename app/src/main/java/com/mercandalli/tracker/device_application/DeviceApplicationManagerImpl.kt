package com.mercandalli.tracker.device_application

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
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
    private val deviceStatsPermissionListeners = ArrayList<DeviceApplicationManager.DeviceStatsPermissionListener>()
    private val deviceApplications = ArrayList<DeviceApplication>()
    private var needStatsPermission = needUsageStatsPermission()
    private var deviceApplicationsDrawables = HashMap<String, Drawable>()

    init {
        refreshDeviceApplications()
    }

    override fun getDeviceApplications(): List<DeviceApplication> {
        return deviceApplications
    }

    override fun getDrawable(packageName: String): Drawable? {
        return deviceApplicationsDrawables[packageName]
    }

    override fun refreshDeviceApplications() {
        Thread(Runnable {
            val deviceApplicationsSync = getDeviceApplicationsSync()
            synchronized(deviceApplications) {
                deviceApplications.clear()
                deviceApplications.addAll(deviceApplicationsSync)
                notifyDeviceApplicationsListeners()
            }
        }).start()
    }

    override fun needUsageStatsPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        val needStatsPermission = createUsageStats().isEmpty()
        if (this.needStatsPermission != needStatsPermission) {
            this.needStatsPermission = needStatsPermission
            notifyDeviceStatsPermissionListeners()
        }
        return needStatsPermission
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

    override fun registerDeviceStatsPermissionListener(listener: DeviceApplicationManager.DeviceStatsPermissionListener) {
        if (deviceStatsPermissionListeners.contains(listener)) {
            return
        }
        deviceStatsPermissionListeners.add(listener)
    }

    override fun unregisterDeviceStatsPermissionListener(listener: DeviceApplicationManager.DeviceStatsPermissionListener) {
        deviceStatsPermissionListeners.remove(listener)
    }

    private fun getDeviceApplicationsSync(): List<DeviceApplication> {
        val sortingNativeFromUserApp = DeviceApplicationUtils.sortingNativeFromUserApp(packageManager, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = ArrayList<DeviceApplication>()
            val queryUsageStats: Map<String, UsageStats> = createUsageStats()

            for (deviceApplication in sortingNativeFromUserApp) {
                val usageStats = queryUsageStats[deviceApplication.`package`]
                val totalTimeInForeground = usageStats?.totalTimeInForeground ?: 0
                val lastLaunch = usageStats?.lastTimeUsed ?: 0
                deviceApplicationsDrawables[deviceApplication.`package`] = deviceApplication.icon
                result.add(DeviceApplication(
                        deviceApplication.kindInstallation,
                        deviceApplication.androidAppName,
                        deviceApplication.`package`,
                        deviceApplication.versionCode,
                        deviceApplication.versionName,
                        deviceApplication.installedAt,
                        deviceApplication.updatedAt,
                        totalTimeInForeground,
                        lastLaunch))
            }
            return result
                    .sortedWith(compareBy({ it.totalTimeInForeground }, { it.installedAt }))
                    .reversed()
        }
        return sortingNativeFromUserApp
                .sortedWith(compareByDescending({ it.installedAt }))
                .map {
                    deviceApplicationsDrawables[it.`package`] = it.icon
                    DeviceApplication(
                            it.kindInstallation,
                            it.androidAppName,
                            it.`package`,
                            it.versionCode,
                            it.versionName,
                            it.installedAt,
                            it.updatedAt,
                            it.totalTimeInForeground,
                            it.lastLaunch)
                }
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

    private fun notifyDeviceApplicationsListeners() {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyDeviceApplicationsListeners() })
            return
        }
        synchronized(deviceApplicationsListeners) {
            for (listener in deviceApplicationsListeners) {
                listener.onDeviceApplicationsChanged()
            }
        }
    }

    private fun notifyDeviceStatsPermissionListeners() {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyDeviceStatsPermissionListeners() })
            return
        }
        synchronized(deviceStatsPermissionListeners) {
            for (listener in deviceStatsPermissionListeners) {
                listener.onDeviceStatsPermissionChanged()
            }
        }
    }

    interface Delegate {
        fun requestUsagePermission()
    }
}