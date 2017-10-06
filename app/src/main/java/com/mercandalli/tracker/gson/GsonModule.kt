package com.mercandalli.tracker.gson

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GsonModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}