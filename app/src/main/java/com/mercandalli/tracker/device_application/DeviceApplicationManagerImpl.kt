package com.mercandalli.tracker.device_application

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import java.util.*
import kotlin.collections.ArrayList

internal class DeviceApplicationManagerImpl constructor(
        private val packageManager: PackageManager,
        private val activityManager: ActivityManager,
        private val usageStatsManager: UsageStatsManager,
        private val delegate: Delegate) : DeviceApplicationManager {

    override fun getDeviceApplications(): List<DeviceApplication> {
        val sortingNativeFromUserApp = AppUtils.sortingNativeFromUserApp(packageManager, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val queryUsageStats = createUsageStats()

            val result = ArrayList<DeviceApplication>()
            for (deviceApplication in sortingNativeFromUserApp) {
                val nbLaunch = queryUsageStats.count { it.packageName == deviceApplication.`package` }
                result.add(DeviceApplication(
                        deviceApplication.kindInstallation,
                        deviceApplication.androidAppName,
                        deviceApplication.`package`,
                        deviceApplication.versionCode,
                        deviceApplication.versionName,
                        deviceApplication.installedAt,
                        deviceApplication.updatedAt,
                        nbLaunch))
            }
            return result.sortedWith(compareBy({ it.nbLaunch }, { it.installedAt }))
        }
        return sortingNativeFromUserApp.sortedWith(compareBy({ it.installedAt }))
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createUsageStats(): List<UsageStats> {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -1)
        val startTime = calendar.timeInMillis
        return usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_YEARLY,
                startTime,
                endTime)
    }

    interface Delegate {
        fun requestUsagePermission()
    }
}