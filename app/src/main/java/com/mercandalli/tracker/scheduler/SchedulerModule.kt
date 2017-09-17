package com.mercandalli.tracker.scheduler

import android.app.job.JobScheduler
import android.content.Context
import android.os.Build
import com.mercandalli.tracker.main.TrackerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SchedulerModule {

    @Singleton
    @Provides
    internal fun provideSchedulerPeriodicTrigger(
            application: TrackerApplication): SchedulerPeriodicTrigger {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val jobScheduler = application.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            SchedulerPeriodicTriggerImpl21(
                    jobScheduler,
                    application.packageName)
        } else {
            object : SchedulerPeriodicTrigger {
                override fun setup() {}

                override fun onTriggered() {}

                override fun unregisterScheduleListener(listener: SchedulerPeriodicTrigger.ScheduleListener) {}

                override fun registerScheduleListener(listener: SchedulerPeriodicTrigger.ScheduleListener) {}
            }
        }
    }
}
