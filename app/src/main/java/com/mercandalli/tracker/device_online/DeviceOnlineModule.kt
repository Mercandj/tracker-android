package com.mercandalli.tracker.device_online

import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.mercandalli.tracker.device_specs.DeviceSpecsManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceOnlineModule {

    @Singleton
    @Provides
    fun provideDeviceOnlineManager(
            gson: Gson,
            deviceSpecsManager: DeviceSpecsManager): DeviceOnlineManager {
        val firebaseStorage = FirebaseStorage.getInstance()
        val deviceSpecsConvertor = DeviceSpecsConverter(gson)
        val deviceFamiliesConvertor = DeviceFamiliesConverter(gson)
        return DeviceOnlineManagerImpl(
                firebaseStorage,
                deviceSpecsConvertor,
                deviceFamiliesConvertor,
                deviceSpecsManager)
    }
}