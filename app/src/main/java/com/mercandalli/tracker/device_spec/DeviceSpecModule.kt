package com.mercandalli.tracker.device_spec

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
            rootManager: RootManager): DeviceSpecsManager {
        val deviceId = DeviceSpecUtils.getDeviceId(application)
        val deviceEmulator = DeviceSpecUtils.isEmulator()
        val deviceRooted = rootManager.isRooted()
        return DeviceSpecManagerImpl(deviceId, deviceEmulator, deviceRooted)
    }
}