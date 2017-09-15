package com.mercandalli.tracker.network

import com.mercandalli.tracker.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

internal object NetworkProvider {

    private var sOkHttpClient: OkHttpClient? = null

    fun provideOkHttpClient(): OkHttpClient {
        if (sOkHttpClient == null) {
            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(interceptor)
            }

            builder.connectTimeout(15, TimeUnit.SECONDS)
            sOkHttpClient = builder.build()
        }
        return sOkHttpClient as OkHttpClient
    }
}
