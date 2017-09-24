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

        private val CLOUD_MESSAGING_API_KEY = "AAAAy9qyE3M:APA91bEt8F9u7r_N8CL4mLvYUDPAJaAClu_GsiDavD0-7VmesKYGkSHx2jAPja8sSuCQ8QV4Q4Y1ZzAi8ykrdA7sQ2R_UhS6-BeAX8fzGrmafCeNcxZ9nfQ1HdGQJLJpDaTkRP_0by4c"
    }
}
