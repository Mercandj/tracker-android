package com.mercandalli.tracker.main

import com.mercandalli.tracker.location.LocationManager
import com.mercandalli.tracker.location.LocationModule
import com.mercandalli.tracker.location.LocationRepository
import com.mercandalli.tracker.main_thread.MainThreadModule
import com.mercandalli.tracker.main_thread.MainThreadPost
import com.mercandalli.tracker.network.NetworkModule
import com.mercandalli.tracker.push.PushModule
import com.mercandalli.tracker.push.PushSenderManager
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        LocationModule::class,
        MainThreadModule::class,
        NetworkModule::class,
        PushModule::class,
        TrackerModule::class)
)
interface TrackerComponent {

    fun provideLocationManager(): LocationManager

    fun provideLocationRepository(): LocationRepository

    fun provideMainThreadPost(): MainThreadPost

    fun provideOkHttpClient(): OkHttpClient

    fun providePushSenderManager(): PushSenderManager

    fun provideTrackerApplication(): TrackerApplication
}