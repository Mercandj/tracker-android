package com.mercandalli.tracker.scheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.mercandalli.tracker.common.Preconditions
import java.util.concurrent.TimeUnit

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
internal class SchedulerPeriodicTriggerImpl21(
        private val jobScheduler: JobScheduler,
        private val packageName: String
) : SchedulerPeriodicTrigger {

    private val JOB_INFO_ID: Int = 13
    private val PLANNED_SCHEDULED_TIMER_INTERVAL = TimeUnit.HOURS.toMillis(1)
    private val scheduleListeners = ArrayList<SchedulerPeriodicTrigger.ScheduleListener>()
    private var isSetup = false

    init {
        Preconditions.checkNotNull(jobScheduler)
        Preconditions.checkNotNull(packageName)
    }

    override fun setup() {
        if (isSetup) {
            return
        }
        // Do not reschedule pending job.
        if (isJobPending(jobScheduler, JOB_INFO_ID)) {
            return
        }

        // As we don't want to add the RECEIVE_BOOT_COMPLETED permission in the lib manifest,
        // we couldn't use the method JobInfo.Builder.setPersisted(true) allowing the device reboot
        // persistence. Doesn't matter because any new collect will re-setup this job scheduler.
        val jobInfo = JobInfo.Builder(JOB_INFO_ID, ComponentName(
                packageName,
                JobSchedulerService::class.java.name))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPeriodic(PLANNED_SCHEDULED_TIMER_INTERVAL)
                .build()
        try {
            val jobId = jobScheduler.schedule(jobInfo)
            if (jobId <= 0) {
                // an error occurred
                Log.e(TAG, "JobScheduler : invalid parameter was supplied. Run-time for your job is" +
                        "too short, or perhaps the system can't resolve the requisite JobService in" +
                        "your package. ")
                return
            }
            isSetup = true
        } catch (e: IllegalArgumentException) {
            // https://fabric.io/djit3/android/apps/com.edjing.edjingdjturntable/issues/57917242ffcdc042502bb70c
            Log.e(TAG, e.message)
        }
    }

    override fun onTriggered() {
        for (listener in scheduleListeners) {
            listener.onTriggered()
        }
    }

    override fun registerScheduleListener(listener: SchedulerPeriodicTrigger.ScheduleListener) {
        if (scheduleListeners.contains(listener)) {
            return
        }
        scheduleListeners.add(listener)
    }

    override fun unregisterScheduleListener(listener: SchedulerPeriodicTrigger.ScheduleListener) {
        scheduleListeners.remove(listener)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun isJobPending(jobScheduler: JobScheduler, jobInfoId: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return isJobPendingApi24(jobScheduler, jobInfoId)
        }
        val allPendingJobs = jobScheduler.allPendingJobs
        return allPendingJobs.any { it != null && it.id == jobInfoId }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun isJobPendingApi24(jobScheduler: JobScheduler, jobInfoId: Int): Boolean {
        return jobScheduler.getPendingJob(jobInfoId) != null
    }
}
