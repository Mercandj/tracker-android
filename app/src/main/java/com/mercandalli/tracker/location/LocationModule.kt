package com.mercandalli.tracker.location

import android.arch.persistence.room.Room
import android.content.Context
import android.os.AsyncTask
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main_thread.MainThreadPost
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class LocationModule {

    @Provides
    @Singleton
    fun provideLocationManager(
            application: TrackerApplication,
            locationRepository: LocationRepository,
            mainThreadPost: MainThreadPost): LocationManager {
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return LocationManagerImpl(
                locationManager,
                locationRepository,
                mainThreadPost)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
            application: TrackerApplication,
            mainThreadPost: MainThreadPost): LocationRepository {

        val locationDatabase = Room.databaseBuilder(
                application,
                LocationDatabase::class.java,
                "location_database").build()

        val locationDao = locationDatabase.locationDao()

        return LocationRepositoryDatabase(mainThreadPost, locationDao, AsyncTask.THREAD_POOL_EXECUTOR)
    }
}