package com.mercandalli.tracker.main

import com.google.gson.Gson
import com.mercandalli.tracker.cloud_messaging.CloudMessagingIdManager
import com.mercandalli.tracker.cloud_messaging.CloudMessagingManager
import com.mercandalli.tracker.cloud_messaging.CloudMessagingModule
import com.mercandalli.tracker.device_application.DeviceApplicationManager
import com.mercandalli.tracker.device_application.DeviceApplicationModule
import com.mercandalli.tracker.device_online.DeviceOnlineManager
import com.mercandalli.tracker.device_online.DeviceOnlineModule
import com.mercandalli.tracker.device_spec.DeviceSpecsManager
import com.mercandalli.tracker.device_spec.DeviceSpecModule
import com.mercandalli.tracker.firebase.FirebaseDatabaseManager
import com.mercandalli.tracker.firebase.FirebaseModule
import com.mercandalli.tracker.firebase.FirebaseStorageManager
import com.mercandalli.tracker.gson.GsonModule
import com.mercandalli.tracker.location.LocationManager
import com.mercandalli.tracker.location.LocationModule
import com.mercandalli.tracker.location.LocationRepository
import com.mercandalli.tracker.main_thread.MainThreadModule
import com.mercandalli.tracker.main_thread.MainThreadPost
import com.mercandalli.tracker.network.NetworkModule
import com.mercandalli.tracker.notification.NotificationManager
import com.mercandalli.tracker.notification.NotificationModule
import com.mercandalli.tracker.push.PushModule
import com.mercandalli.tracker.push.PushSenderManager
import com.mercandalli.tracker.root.RootManager
import com.mercandalli.tracker.root.RootModule
import com.mercandalli.tracker.scheduler.SchedulerModule
import com.mercandalli.tracker.scheduler.SchedulerPeriodicTrigger
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        CloudMessagingModule::class,
        DeviceApplicationModule::class,
        DeviceOnlineModule::class,
        DeviceSpecModule::class,
        FirebaseModule::class,
        GsonModule::class,
        LocationModule::class,
        MainThreadModule::class,
        NetworkModule::class,
        NotificationModule::class,
        PushModule::class,
        RootModule::class,
        SchedulerModule::class,
        TrackerModule::class)
)
interface TrackerComponent {

    fun provideCloudMessagingIdManager(): CloudMessagingIdManager

    fun provideCloudMessagingManager(): CloudMessagingManager

    fun provideDeviceApplicationManager(): DeviceApplicationManager

    fun provideDeviceOnlineManager(): DeviceOnlineManager

    fun provideDeviceSpecsManager(): DeviceSpecsManager

    fun provideFirebaseDatabaseManager(): FirebaseDatabaseManager

    fun provideFirebaseStorageManager(): FirebaseStorageManager

    fun provideGson(): Gson

    fun provideLocationManager(): LocationManager

    fun provideLocationRepository(): LocationRepository

    fun provideMainThreadPost(): MainThreadPost

    fun provideNotificationManager(): NotificationManager

    fun provideOkHttpClient(): OkHttpClient

    fun providePushSenderManager(): PushSenderManager

    fun provideRootManager(): RootManager

    fun provideSchedulerPeriodicTrigger(): SchedulerPeriodicTrigger

    fun provideTrackerApplication(): TrackerApplication
}