package com.mercandalli.tracker.device

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.mercandalli.tracker.R
import com.mercandalli.tracker.device_online.DeviceOnlineManager
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main.TrackerComponent

class DeviceOnlineView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var recyclerView: RecyclerView? = null
    private val recyclerAdapter = createRecyclerAdapter()

    private val appComponent: TrackerComponent = TrackerApplication.appComponent
    private val deviceOnlineManager = appComponent.provideDeviceOnlineManager()
    private val deviceSpecsListener = createDeviceSpecsListener()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device, this)
        orientation = VERTICAL
        recyclerView = findViewById(R.id.view_device_list)
        recyclerView!!.layoutManager = createLayoutManager(context)
        recyclerView!!.adapter = recyclerAdapter

        val title = findViewById<TextView>(R.id.view_device_title)
        title.text = "All devices"

        recyclerAdapter.setDeviceSpecs(deviceOnlineManager.getDeviceSpecsSync())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        deviceOnlineManager.registerDeviceSpecsListener(deviceSpecsListener)
    }

    override fun onDetachedFromWindow() {
        deviceOnlineManager.unregisterDeviceSpecsListener(deviceSpecsListener)
        super.onDetachedFromWindow()
    }

    private fun createDeviceSpecsListener(): DeviceOnlineManager.OnDeviceSpecsListener {
        return object : DeviceOnlineManager.OnDeviceSpecsListener {
            override fun onDeviceSpecsChanged() {
                recyclerAdapter.setDeviceSpecs(deviceOnlineManager.getDeviceSpecsSync())
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