package com.mercandalli.tracker.main

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule(
        private val application: MainApplication) {

    @Provides
    @Singleton
    fun provideTrackerApplication(): MainApplication = application
}
