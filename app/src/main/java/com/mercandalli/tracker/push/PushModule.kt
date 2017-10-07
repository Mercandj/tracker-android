package com.mercandalli.tracker.push

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class PushModule {

    @Singleton
    @Provides
    internal fun providePushSenderManager(
            okHttpClient: OkHttpClient,
            gson: Gson): PushSenderManager {
        return PushSenderManagerNetwork(
                okHttpClient,
                gson,
                CLOUD_MESSAGING_API_KEY)
    }

    companion object {

        private val CLOUD_MESSAGING_API_KEY = "AAAAFnjUxws:APA91bGesReILlGlDKySySV0GWNxy4PMOboBdOB398p7Z-3o81A8a5l2E7fSgSz8u7ElJDqOOA4Kix_WBZndmSFJbadJmlC5gKQlRe5ChzK_FkdE4sgm9nOcdXtPLM5hFNd0K29pzJkV"
    }
}
