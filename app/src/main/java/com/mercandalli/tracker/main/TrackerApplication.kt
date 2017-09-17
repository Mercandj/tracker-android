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

        setupScheduler()
    }

    /**
     * Set up the application dagger graph. See [TrackerComponent].
     */
    private fun setupGraph() {
        appComponent = DaggerTrackerComponent.builder()
                .trackerModule(TrackerModule(this))
                .build()
    }

    private fun setupScheduler() {
        val schedulerPeriodicTrigger = appComponent.provideSchedulerPeriodicTrigger()
        val scheduleListener = object : SchedulerPeriodicTrigger.ScheduleListener {
            override fun onTriggered() {
                appComponent.provideLocationManager().requestSingleUpdate()
            }
        }
        schedulerPeriodicTrigger.registerScheduleListener(scheduleListener)
        schedulerPeriodicTrigger.setup()
    }
}
