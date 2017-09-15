package com.mercandalli.tracker.main_thread

import android.os.Handler
import android.os.Looper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainThreadModule {

    @Provides
    @Singleton
    fun provideMainThreadPost(): MainThreadPost {
        val mainLooper = Looper.getMainLooper()
        return MainThreadPostImpl(
                mainLooper.thread,
                Handler(mainLooper))
    }
}