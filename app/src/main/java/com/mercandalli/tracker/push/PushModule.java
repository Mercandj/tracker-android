package com.mercandalli.tracker.push;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class PushModule {

    private static final String CLOUD_MESSAGING_API_KEY = "AAAAy9qyE3M:APA91bEt8F9u7r_N8CL4mLvYUDPAJaAClu_GsiDavD0-7VmesKYGkSHx2jAPja8sSuCQ8QV4Q4Y1ZzAi8ykrdA7sQ2R_UhS6-BeAX8fzGrmafCeNcxZ9nfQ1HdGQJLJpDaTkRP_0by4c";

    @Singleton
    @Provides
    PushSenderManager providePushSenderManager(OkHttpClient okHttpClient) {
        return new PushSenderManagerNetwork(
                okHttpClient,
                CLOUD_MESSAGING_API_KEY);
    }
}
