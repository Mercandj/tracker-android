package com.mercandalli.tracker.device_application

import android.app.ActivityManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import com.mercandalli.tracker.device_application.DeviceApplicationManagerImpl.Delegate
import com.mercandalli.tracker.device_spec.DeviceSpecManager
import com.mercandalli.tracker.main.MainApplication
import com.mercandalli.tracker.main_thread.MainThreadPost
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceApplicationModule {

    @Singleton
    @Provides
    fun provideDeviceApplicationManager(
            application: MainApplication,
            deviceSpecManager: DeviceSpecManager,
            mainThreadPost: MainThreadPost): DeviceApplicationManager {
        val activityManager = application.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val usageStatsManager = application.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        return DeviceApplicationManagerImpl(
                deviceSpecManager.getDeviceSpec().deviceTrackerId,
                application.packageManager,
                activityManager,
                usageStatsManager,
                mainThreadPost,
                object : Delegate {
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun requestUsagePermission() {
                        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        application.startActivity(intent)
                    }
                })
    }
}