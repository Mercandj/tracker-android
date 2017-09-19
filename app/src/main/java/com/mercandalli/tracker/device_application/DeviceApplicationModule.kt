package com.mercandalli.tracker.device_application

import com.mercandalli.tracker.main.TrackerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceApplicationModule {

    @Singleton
    @Provides
    fun provideDeviceApplicationManager(application: TrackerApplication): DeviceApplicationManager {
        return DeviceApplicationManagerImpl(application.packageManager)
    }
}