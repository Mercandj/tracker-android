package com.mercandalli.tracker.device_online

import com.mercandalli.tracker.cloud_messaging.CloudMessagingIdManager
import com.mercandalli.tracker.device.DeviceRepository
import com.mercandalli.tracker.device_application.DeviceApplicationManager
import com.mercandalli.tracker.device_spec.DeviceSpecManager
import com.mercandalli.tracker.firebase.FirebaseDatabaseManager
import com.mercandalli.tracker.firebase.FirebaseStorageManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceOnlineModule {

    @Singleton
    @Provides
    fun provideDeviceOnlineManager(
            deviceSpecManager: DeviceSpecManager,
            deviceApplicationManager: DeviceApplicationManager,
            deviceRepository: DeviceRepository,
            firebaseStorageManager: FirebaseStorageManager,
            firebaseDatabaseManager: FirebaseDatabaseManager,
            cloudMessagingIdManager: CloudMessagingIdManager): DeviceOnlineManager {
        return DeviceOnlineManagerImpl(
                firebaseStorageManager,
                firebaseDatabaseManager,
                deviceSpecManager,
                deviceApplicationManager,
                deviceRepository,
                cloudMessagingIdManager)
    }
}