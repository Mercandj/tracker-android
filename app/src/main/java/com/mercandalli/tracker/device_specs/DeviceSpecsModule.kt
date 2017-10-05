package com.mercandalli.tracker.device_specs

import android.provider.Settings.Secure
import com.mercandalli.tracker.main.TrackerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DeviceSpecsModule {

    @Singleton
    @Provides
    fun provideDeviceSpecsManager(application: TrackerApplication): DeviceSpecsManager {
        val androidId = Secure.getString(application.getContentResolver(), Secure.ANDROID_ID)
        return DeviceSpecsManagerImpl(androidId)
    }
}