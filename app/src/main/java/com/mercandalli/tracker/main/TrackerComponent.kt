package com.mercandalli.tracker.main

import com.mercandalli.tracker.location.CurrentLocationManager
import com.mercandalli.tracker.location.CurrentLocationModule
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
        MainThreadModule::class,
        CurrentLocationModule::class,
        NetworkModule::class,
        PushModule::class,
        TrackerModule::class)
)
interface TrackerComponent {

    fun provideMainThreadPost(): MainThreadPost

    fun provideCurrentLocationManager(): CurrentLocationManager

    fun provideOkHttpClient(): OkHttpClient

    fun providePushSenderManager(): PushSenderManager

    fun provideTrackerApplication(): TrackerApplication
}