package com.mercandalli.tracker.device

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.mercandalli.tracker.device_application.DeviceApplication
import com.mercandalli.tracker.device_application.DeviceApplicationsPreviewCard
import com.mercandalli.tracker.device_spec.DeviceSpec
import com.mercandalli.tracker.device_spec.DeviceSpecCard

internal class DeviceRecyclerAdapter : ListDelegationAdapter<List<Any>>() {

    private var deviceSpec: DeviceSpec? = null
    private var deviceSpecs = ArrayList<DeviceSpec>()
    private var deviceApplications = ArrayList<DeviceApplication>()
    private var setDeviceApplicationCalled = false

    init {
        delegatesManager.addDelegate(DeviceSpecsAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(DeviceApplicationsPreviewAdapterDelegate() as AdapterDelegate<List<Any>>)
    }

    fun setDeviceSpec(deviceSpec: DeviceSpec) {
        this.deviceSpec = deviceSpec
        syncList()
    }

    fun setDeviceSpecs(deviceSpecs: List<DeviceSpec>) {
        this.deviceSpecs.clear()
        this.deviceSpecs.addAll(deviceSpecs)
        syncList()
    }

    fun setDeviceApplications(deviceApplications: List<DeviceApplication>) {
        setDeviceApplicationCalled = true
        this.deviceApplications.clear()
        this.deviceApplications.addAll(deviceApplications)
        syncList()
    }

    private fun syncList() {
        val list = ArrayList<Any>()
        list.addAll(deviceSpecs)
        deviceSpec?.let { list.add(it) }
        if (setDeviceApplicationCalled) {
            list.add(deviceApplications)
        }
        setItems(list)
        notifyDataSetChanged()
    }

    private class DeviceSpecsAdapterDelegate :
            AbsListItemAdapterDelegate<Any, Any, DeviceSpecsViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is DeviceSpec
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): DeviceSpecsViewHolder {
            val context = viewGroup.context
            val deviceSpecsCard = DeviceSpecCard(context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            deviceSpecsCard.layoutParams = layoutParams
            return DeviceSpecsViewHolder(deviceSpecsCard)
        }

        override fun onBindViewHolder(
                model: Any, deviceSpecsViewHolder: DeviceSpecsViewHolder, list: List<Any>) {
            deviceSpecsViewHolder.bind(model as DeviceSpec)
        }
    }

    private class DeviceSpecsViewHolder(
            private val view: DeviceSpecCard) :
            RecyclerView.ViewHolder(view) {
        fun bind(deviceSpec: DeviceSpec) {
            view.setDeviceSpecs(deviceSpec)
        }
    }

    private class DeviceApplicationsPreviewAdapterDelegate :
            AbsListItemAdapterDelegate<Any, Any, DeviceApplicationsPreviewViewHolder>() {

        private val LIST_DEVICE_APPLICATION = object : TypeToken<List<DeviceApplication>>() {
        }.rawType

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return LIST_DEVICE_APPLICATION.isAssignableFrom(o.javaClass)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): DeviceApplicationsPreviewViewHolder {
            val context = viewGroup.context
            val deviceApplicationsPreviewCard = DeviceApplicationsPreviewCard(context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            deviceApplicationsPreviewCard.layoutParams = layoutParams
            return DeviceApplicationsPreviewViewHolder(deviceApplicationsPreviewCard)
        }

        @Suppress("UNCHECKED_CAST")
        override fun onBindViewHolder(
                model: Any, deviceApplicationsPreviewViewHolder: DeviceApplicationsPreviewViewHolder, list: List<Any>) {
            deviceApplicationsPreviewViewHolder.bind(model as List<DeviceApplication>)
        }
    }

    private class DeviceApplicationsPreviewViewHolder(
            private val view: DeviceApplicationsPreviewCard) :
            RecyclerView.ViewHolder(view) {
        fun bind(deviceApplications: List<DeviceApplication>) {
            view.setDeviceApplications(deviceApplications)
        }
    }
}
