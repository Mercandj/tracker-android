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
import com.mercandalli.tracker.device.DeviceRepository
import com.mercandalli.tracker.main.MainApplication
import com.mercandalli.tracker.main.MainComponent

class DeviceApplicationsActivity : AppCompatActivity() {

    companion object {

        private val EXTRA_DEVICE_TRACKER_ID = "DeviceApplicationsActivity.Extra.EXTRA_DEVICE_TRACKER_ID"

        fun start(context: Context, deviceTrackerId: String) {
            Preconditions.checkNotNull(context)
            val intent = Intent(context, DeviceApplicationsActivity::class.java)
            intent.putExtra(EXTRA_DEVICE_TRACKER_ID, deviceTrackerId)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }

    private var recyclerView: RecyclerView? = null
    private val recyclerAdapter = createRecyclerAdapter()

    private val appComponent: MainComponent = MainApplication.appComponent
    private val deviceRepository = appComponent.provideDeviceRepository()
    private val deviceListener = createDeviceListener()
    private var deviceTrackerId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_applications)
        setSupportActionBar(findViewById(R.id.activity_device_applications_toolbar))
        deviceTrackerId = intent.getStringExtra(EXTRA_DEVICE_TRACKER_ID)

        recyclerView = findViewById(R.id.activity_device_applications_list)
        recyclerView!!.layoutManager = createLayoutManager(this)
        recyclerView!!.adapter = recyclerAdapter

        deviceRepository.registerDevicesListener(deviceListener)
        updateApps()
    }

    override fun onDestroy() {
        deviceRepository.unregisterDevicesListener(deviceListener)
        super.onDestroy()
    }

    private fun createDeviceListener(): DeviceRepository.DevicesListener {
        return object : DeviceRepository.DevicesListener {
            override fun onDevicesChanged() {
                updateApps()
            }
        }
    }

    private fun createLayoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 4)
    }

    private fun createRecyclerAdapter(): DeviceApplicationRecyclerAdapter {
        return DeviceApplicationRecyclerAdapter()
    }

    private fun updateApps() {
        if (deviceTrackerId == null) {
            return
        }
        val device = deviceRepository.getDevice(deviceTrackerId!!) ?: return
        recyclerAdapter.setDeviceApplications(device.deviceApplications)
    }
}