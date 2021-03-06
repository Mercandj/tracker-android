package com.mercandalli.tracker.notification

import android.content.Context
import com.mercandalli.tracker.main.MainApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationModule {

    @Singleton
    @Provides
    internal fun provideNotificationManager(application: MainApplication): NotificationManager {
        val notificationSystemService = application.getSystemService(Context.NOTIFICATION_SERVICE)
        val notificationManager = notificationSystemService as android.app.NotificationManager
        return NotificationManagerImpl(application, notificationManager)
    }
}
