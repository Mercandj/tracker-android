package com.mercandalli.tracker.main

import com.mercandalli.tracker.device_specs.DeviceSpecsManager
import com.mercandalli.tracker.device_specs.DeviceSpecsModule
import com.mercandalli.tracker.location.LocationManager
import com.mercandalli.tracker.location.LocationModule
import com.mercandalli.tracker.location.LocationRepository
import com.mercandalli.tracker.main_thread.MainThreadModule
import com.mercandalli.tracker.main_thread.MainThreadPost
import com.mercandalli.tracker.network.NetworkModule
import com.mercandalli.tracker.push.PushModule
import com.mercandalli.tracker.push.PushSenderManager
import com.mercandalli.tracker.scheduler.SchedulerModule
import com.mercandalli.tracker.scheduler.SchedulerPeriodicTrigger
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        DeviceSpecsModule::class,
        LocationModule::class,
        MainThreadModule::class,
        NetworkModule::class,
        PushModule::class,
        SchedulerModule::class,
        TrackerModule::class)
)
interface TrackerComponent {

    fun provideDeviceSpecsManager(): DeviceSpecsManager

    fun provideLocationManager(): LocationManager

    fun provideLocationRepository(): LocationRepository

    fun provideMainThreadPost(): MainThreadPost

    fun provideOkHttpClient(): OkHttpClient

    fun providePushSenderManager(): PushSenderManager

    fun provideSchedulerPeriodicTrigger(): SchedulerPeriodicTrigger

    fun provideTrackerApplication(): TrackerApplication
}