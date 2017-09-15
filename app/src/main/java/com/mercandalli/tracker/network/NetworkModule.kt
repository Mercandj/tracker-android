package com.mercandalli.tracker.network

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        return NetworkProvider.provideOkHttpClient()
    }
}
