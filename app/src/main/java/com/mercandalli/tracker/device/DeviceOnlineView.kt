package com.mercandalli.tracker.device

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mercandalli.tracker.R
import com.mercandalli.tracker.device_online.DeviceOnlineManager
import com.mercandalli.tracker.main.MainApplication
import com.mercandalli.tracker.main.MainComponent

class DeviceOnlineView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var recyclerView: RecyclerView? = null
    private val recyclerAdapter = createRecyclerAdapter()

    private val appComponent: MainComponent = MainApplication.appComponent
    private val deviceOnlineManager = appComponent.provideDeviceOnlineManager()
    private val deviceSpecsListener = createDeviceSpecsListener()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device, this)
        orientation = VERTICAL
        recyclerView = findViewById(R.id.view_device_list)
        recyclerView!!.layoutManager = createLayoutManager(context)
        recyclerView!!.adapter = recyclerAdapter

        syncDevices()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        deviceOnlineManager.registerDevicesListener(deviceSpecsListener)
    }

    override fun onDetachedFromWindow() {
        deviceOnlineManager.unregisterDevicesListener(deviceSpecsListener)
        super.onDetachedFromWindow()
    }

    private fun syncDevices() {
        val devices = deviceOnlineManager.getDevicesSync()
        val list = ArrayList<Any>()
        for (device in devices) {
            list.add(
                    if (TextUtils.isEmpty(device.deviceNickname)) device.deviceSpec.deviceTrackerId
                    else device.deviceNickname!!)
            list.add(device.deviceSpec)
            list.add(device.deviceApplications)
        }
        recyclerAdapter.setObjects(list)
    }

    private fun createDeviceSpecsListener(): DeviceOnlineManager.OnDevicesListener {
        return object : DeviceOnlineManager.OnDevicesListener {
            override fun onDevicesChanged() {
                syncDevices()
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