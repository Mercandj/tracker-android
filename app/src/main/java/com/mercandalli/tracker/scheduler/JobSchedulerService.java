package com.mercandalli.tracker.scheduler;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

import com.mercandalli.tracker.main.MainApplication;
import com.mercandalli.tracker.main.MainComponent;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {

    @SuppressWarnings("unused")
    private static final String TAG = "JobSchedulerService";

    @Override
    public boolean onStartJob(JobParameters params) {
        MainComponent appComponent = MainApplication.getAppComponent();
        SchedulerPeriodicTrigger schedulerPeriodicTrigger = appComponent.provideSchedulerPeriodicTrigger();
        schedulerPeriodicTrigger.onTriggered();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
