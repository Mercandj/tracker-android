package com.mercandalli.tracker.user

import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.mercandalli.tracker.device_specs.DeviceSpecsManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserModule {

    @Singleton
    @Provides
    fun provideUserManager(
            gson: Gson,
            deviceSpecsManager: DeviceSpecsManager): UserManager {
        val firebaseStorage = FirebaseStorage.getInstance("gs://tracker-32412.appspot.com")
        val userDeviceSpecsFileCreator = UserDeviceSpecsFileCreator(gson)
        return UserManagerImpl(
                firebaseStorage,
                userDeviceSpecsFileCreator,
                deviceSpecsManager)
    }
}