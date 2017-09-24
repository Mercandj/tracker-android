package com.mercandalli.tracker.device

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mercandalli.tracker.R
import com.mercandalli.tracker.device_application.DeviceApplicationManager.DeviceApplicationsListener
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main.TrackerComponent

class DeviceView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var recyclerView: RecyclerView? = null
    private val deviceRecyclerAdapter = createRecyclerAdapter()

    private val appComponent: TrackerComponent = TrackerApplication.appComponent
    private val deviceSpecsManager = appComponent.provideDeviceSpecsManager()
    private val deviceApplicationsManager = appComponent.provideDeviceApplicationManager()
    private val deviceApplicationsListener = createDeviceApplicationsListener()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device, this)
        orientation = VERTICAL
        recyclerView = findViewById(R.id.view_device_list)
        recyclerView!!.layoutManager = createLayoutManager(context)
        recyclerView!!.adapter = deviceRecyclerAdapter

        deviceRecyclerAdapter.setDeviceSpecs(deviceSpecsManager.getDeviceSpecs())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        deviceApplicationsManager.registerDeviceApplicationsListener(deviceApplicationsListener)
        deviceRecyclerAdapter.setDeviceApplications(deviceApplicationsManager.getDeviceApplications())
    }

    override fun onDetachedFromWindow() {
        deviceApplicationsManager.unregisterDeviceApplicationsListener(deviceApplicationsListener)
        super.onDetachedFromWindow()
    }

    private fun createDeviceApplicationsListener(): DeviceApplicationsListener {
        return object : DeviceApplicationsListener {
            override fun onDeviceApplicationsChanged() {
                deviceRecyclerAdapter.setDeviceApplications(deviceApplicationsManager.getDeviceApplications())
            }
        }
    }

    private fun createLayoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    private fun createRecyclerAdapter(): DeviceRecyclerAdapter {
        return DeviceRecyclerAdapter()
    }
}
