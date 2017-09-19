package com.mercandalli.tracker.device

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mercandalli.tracker.R
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main.TrackerComponent

class DeviceView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var recyclerView: RecyclerView? = null
    private val deviceRecyclerAdapter = createRecyclerAdapter()

    private val appComponent: TrackerComponent = TrackerApplication.appComponent
    private val deviceSpecsManager = appComponent.provideDeviceSpecsManager()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device, this)
        orientation = VERTICAL
        recyclerView = findViewById(R.id.view_device_list)
        recyclerView!!.layoutManager = createLayoutManager(context)
        recyclerView!!.adapter = deviceRecyclerAdapter

        deviceRecyclerAdapter.setDeviceSpecs(deviceSpecsManager.getDeviceSpecs())
    }

    private fun createLayoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    private fun createRecyclerAdapter(): DeviceRecyclerAdapter {
        return DeviceRecyclerAdapter()
    }
}
