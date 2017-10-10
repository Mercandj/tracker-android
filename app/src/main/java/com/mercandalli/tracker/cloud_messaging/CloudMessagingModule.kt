package com.mercandalli.tracker.cloud_messaging

import com.mercandalli.tracker.main.MainApplication
import com.mercandalli.tracker.main_thread.MainThreadPost
import com.mercandalli.tracker.push.PushSenderManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CloudMessagingModule {

    @Singleton
    @Provides
    internal fun provideCloudMessagingIdManager(
            application: MainApplication,
            mainThreadPost: MainThreadPost): CloudMessagingIdManager {
        val delegate = createCloudMessagingIdManagerImplDelegate(application)
        return CloudMessagingIdManagerImpl(
                mainThreadPost,
                delegate)
    }

    @Singleton
    @Provides
    internal fun provideCloudMessagingManager(
            pushSenderManager: PushSenderManager): CloudMessagingManager {
        return CloudMessagingManagerImpl(pushSenderManager)
    }

    private fun createCloudMessagingIdManagerImplDelegate(application: MainApplication):
            CloudMessagingIdManagerImpl.Delegate {
        return object : CloudMessagingIdManagerImpl.Delegate {
            override fun startCloudMessagingIdIntentService() {
                CloudMessagingIdIntentService.start(application)
            }
        }
    }
}
