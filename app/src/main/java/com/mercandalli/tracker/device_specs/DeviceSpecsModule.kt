package com.mercandalli.tracker.device_specs

import com.mercandalli.tracker.main.TrackerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DeviceSpecsModule {

    @Singleton
    @Provides
    fun provideDeviceSpecsManager(application: TrackerApplication): DeviceSpecsManager {
        val deviceId = DeviceSpecsUtils.getDeviceId(application)
        val deviceEmulator = DeviceSpecsUtils.isEmulator()
        return DeviceSpecsManagerImpl(deviceId, deviceEmulator)
    }
}