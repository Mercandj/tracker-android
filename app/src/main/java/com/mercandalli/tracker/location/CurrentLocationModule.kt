package com.mercandalli.tracker.location

import android.content.Context
import android.location.LocationManager
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main_thread.MainThreadPost
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CurrentLocationModule(
        private val application: TrackerApplication,
        private val mainThreadPost: MainThreadPost) {

    @Provides
    @Singleton
    fun provideCurrentLocationManager(): CurrentLocationManager {
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return CurrentLocationManagerImpl(
                locationManager,
                mainThreadPost)
    }
}