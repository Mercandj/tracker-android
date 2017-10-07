package com.mercandalli.tracker.device_specs

import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.root.RootManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DeviceSpecsModule {

    @Singleton
    @Provides
    fun provideDeviceSpecsManager(
            application: TrackerApplication,
            rootManager: RootManager): DeviceSpecsManager {
        val deviceId = DeviceSpecsUtils.getDeviceId(application)
        val deviceEmulator = DeviceSpecsUtils.isEmulator()
        val deviceRooted = rootManager.isRooted()
        return DeviceSpecsManagerImpl(deviceId, deviceEmulator, deviceRooted)
    }
}