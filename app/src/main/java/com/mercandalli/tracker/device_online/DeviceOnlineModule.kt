package com.mercandalli.tracker.device_online

import com.google.gson.Gson
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
            firebaseStorageManager: FirebaseStorageManager,
            firebaseDatabaseManager: FirebaseDatabaseManager): DeviceOnlineManager {
        val deviceSpecsConvertor = DeviceSpecsConverter(gson)
        val deviceFamiliesConvertor = DeviceFamiliesConverter(gson)
        return DeviceOnlineManagerImpl(
                firebaseStorageManager,
                firebaseDatabaseManager,
                deviceSpecsConvertor,
                deviceFamiliesConvertor,
                deviceSpecsManager)
    }
}