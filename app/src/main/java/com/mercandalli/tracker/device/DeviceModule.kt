package com.mercandalli.tracker.device

import com.mercandalli.tracker.device_application.DeviceApplicationManager
import com.mercandalli.tracker.device_spec.DeviceSpecManager
import com.mercandalli.tracker.main_thread.MainThreadPost
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceModule {

    @Singleton
    @Provides
    fun provideDeviceRepository(
            deviceSpecManager: DeviceSpecManager,
            deviceApplicationManager: DeviceApplicationManager,
            mainThreadPost: MainThreadPost): DeviceRepository {
        val device = Device(
                deviceSpecManager.getDeviceSpec(),
                deviceApplicationManager.getDeviceApplications(),
                null,
                null,
                ArrayList())
        return DeviceRepositoryImpl(
                device,
                deviceApplicationManager,
                mainThreadPost)
    }
}