package com.mercandalli.tracker.main

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(TrackerModule::class))
interface TrackerComponent {

    fun provideTrackerApplication(): TrackerApplication
}