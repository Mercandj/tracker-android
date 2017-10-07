package com.mercandalli.tracker.device_online

import com.google.gson.Gson
import com.mercandalli.tracker.device_application.DeviceApplicationManager
import com.mercandalli.tracker.device_specs.DeviceSpecsManager
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
            gson: Gson,
            deviceSpecsManager: DeviceSpecsManager,
            deviceApplicationManager:DeviceApplicationManager,
            firebaseStorageManager: FirebaseStorageManager,
            firebaseDatabaseManager: FirebaseDatabaseManager): DeviceOnlineManager {
        return DeviceOnlineManagerImpl(
                firebaseStorageManager,
                firebaseDatabaseManager,
                deviceSpecsManager,
                deviceApplicationManager)
    }
}