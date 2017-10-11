package com.mercandalli.tracker.location

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.provider.Settings
import com.mercandalli.tracker.location.LocationManagerImpl.Delegate
import com.mercandalli.tracker.main.MainApplication
import com.mercandalli.tracker.main_thread.MainThreadPost
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocationModule {

    @Provides
    @Singleton
    fun provideLocationManager(
            application: MainApplication,
            locationRepository: LocationRepository,
            mainThreadPost: MainThreadPost): LocationManager {
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return LocationManagerImpl(
                locationManager,
                locationRepository,
                mainThreadPost,
                object : Delegate {
                    override fun startLocationSettings() {
                        return application.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                })
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
            application: MainApplication,
            mainThreadPost: MainThreadPost): LocationRepository {
        val locationDatabase = Room.databaseBuilder(
                application,
                LocationDatabase::class.java,
                "location_database").build()
        val locationDao = locationDatabase.locationDao()
        return LocationRepositoryDatabase(
                mainThreadPost,
                locationDao,
                AsyncTask.THREAD_POOL_EXECUTOR)
    }
}