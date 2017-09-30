package com.mercandalli.tracker.device_application

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.mercandalli.tracker.R
import com.mercandalli.tracker.common.Preconditions
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main.TrackerComponent

class DeviceApplicationsActivity : AppCompatActivity() {

    companion object {

        fun start(context: Context) {
            Preconditions.checkNotNull(context)
            val intent = Intent(context, DeviceApplicationsActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }

    private var recyclerView: RecyclerView? = null
    private val recyclerAdapter = createRecyclerAdapter()

    private val appComponent: TrackerComponent = TrackerApplication.appComponent
    private val deviceApplicationsManager = appComponent.provideDeviceApplicationManager()
    private val deviceApplicationsListener = createDeviceApplicationsListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_applications)
        setSupportActionBar(findViewById(R.id.activity_device_applications_toolbar))

        recyclerView = findViewById(R.id.activity_device_applications_list)
        recyclerView!!.layoutManager = createLayoutManager(this)
        recyclerView!!.adapter = recyclerAdapter

        deviceApplicationsManager.registerDeviceApplicationsListener(deviceApplicationsListener)
        recyclerAdapter.setDeviceApplications(deviceApplicationsManager.getDeviceApplications())
    }

    override fun onDestroy() {
        deviceApplicationsManager.unregisterDeviceApplicationsListener(deviceApplicationsListener)
        super.onDestroy()
    }

    private fun createDeviceApplicationsListener(): DeviceApplicationManager.DeviceApplicationsListener {
        return object : DeviceApplicationManager.DeviceApplicationsListener {
            override fun onDeviceApplicationsChanged() {
                recyclerAdapter.setDeviceApplications(deviceApplicationsManager.getDeviceApplications())
            }
        }
    }

    private fun createLayoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 4)
    }

    private fun createRecyclerAdapter(): DeviceApplicationRecyclerAdapter {
        return DeviceApplicationRecyclerAdapter()
    }
}