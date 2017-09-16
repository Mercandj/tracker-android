package com.mercandalli.tracker.location

import android.content.Context
import android.location.LocationManager
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main_thread.MainThreadPost
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CurrentLocationModule {

    @Provides
    @Singleton
    fun provideCurrentLocationManager(
            mainThreadPost: MainThreadPost,
            application: TrackerApplication): CurrentLocationManager {
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return CurrentLocationManagerImpl(
                locationManager,
                mainThreadPost)
    }
}