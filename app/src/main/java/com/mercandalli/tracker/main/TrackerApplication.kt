package com.mercandalli.tracker.main

import android.app.Application
import com.mercandalli.tracker.scheduler.SchedulerPeriodicTrigger

class TrackerApplication : Application() {

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var appComponent: TrackerComponent
    }

    override fun onCreate() {
        super.onCreate()

        setupGraph()
    }

    /**
     * Set up the application dagger graph. See [TrackerComponent].
     */
    private fun setupGraph() {
        appComponent = DaggerTrackerComponent.builder()
                .trackerModule(TrackerModule(this))
                .build()
    }
}
