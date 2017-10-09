package com.mercandalli.tracker.device_spec

import com.mercandalli.tracker.R
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.root.RootManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceSpecModule {

    @Singleton
    @Provides
    fun provideDeviceSpecsManager(
            application: TrackerApplication,
            rootManager: RootManager): DeviceSpecManager {
        val deviceId = DeviceSpecUtils.getDeviceId(application)
        val deviceDensity = application.getString(R.string.device_spec_density)
        val deviceEmulator = DeviceSpecUtils.isEmulator()
        val deviceRooted = rootManager.isRooted()
        val delegate: DeviceSpecManagerImpl.Delegate = object : DeviceSpecManagerImpl.Delegate {
            override fun getMacAddress(): String? {
                return DeviceSpecUtils.getMacAddress(application)
            }

            override fun getBatteryPercent(): Float {
                return DeviceSpecUtils.getBatteryPercent(application)
            }
        }
        return DeviceSpecManagerImpl(
                deviceId,
                deviceDensity,
                deviceEmulator,
                deviceRooted,
                delegate)
    }
}