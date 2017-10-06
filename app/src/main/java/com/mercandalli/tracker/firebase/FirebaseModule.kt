package com.mercandalli.tracker.firebase

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabaseManager(): FirebaseDatabaseManager {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        return FirebaseDatabaseManagerImpl(
                firebaseDatabase)
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageManager(): FirebaseStorageManager {
        val firebaseStorage = FirebaseStorage.getInstance()
        return FirebaseStorageManagerImpl(
                firebaseStorage)
    }
}