package com.mercandalli.tracker.main

import android.app.Application

class MainApplication : Application() {

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var appComponent: MainComponent
    }

    override fun onCreate() {
        super.onCreate()

        setupGraph()

        appComponent.provideCloudMessagingIdManager().getCloudMessagingId()
    }

    /**
     * Set up the application dagger graph. See [MainComponent].
     */
    private fun setupGraph() {
        appComponent = DaggerMainComponent.builder()
                .mainModule(MainModule(this))
                .build()
    }
}
