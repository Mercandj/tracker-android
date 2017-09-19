package com.mercandalli.tracker.device_specs

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceSpecsModule {

    @Singleton
    @Provides
    fun provideDeviceSpecsManager(): DeviceSpecsManager {
        return DeviceSpecsManagerImpl()
    }
}