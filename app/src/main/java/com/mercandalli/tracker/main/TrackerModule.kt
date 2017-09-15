package com.mercandalli.tracker.main

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TrackerModule(
        private val application: TrackerApplication) {

    @Provides
    @Singleton
    fun provideTrackerApplication(): TrackerApplication = application
}
