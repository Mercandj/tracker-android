package com.mercandalli.tracker.device

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.mercandalli.tracker.device_application.DeviceApplication
import com.mercandalli.tracker.device_application.DeviceApplicationsPreviewCard
import com.mercandalli.tracker.device_specs.DeviceSpecs
import com.mercandalli.tracker.device_specs.DeviceSpecsCard


internal class DeviceRecyclerAdapter : ListDelegationAdapter<List<Any>>() {

    private var deviceSpecs: DeviceSpecs? = null
    private var deviceApplications = ArrayList<DeviceApplication>()

    init {
        delegatesManager.addDelegate(DeviceSpecsAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(DeviceApplicationsPreviewAdapterDelegate() as AdapterDelegate<List<Any>>)
    }

    fun setDeviceSpecs(deviceSpecs: DeviceSpecs) {
        this.deviceSpecs = deviceSpecs
        syncList()
    }

    fun setDeviceApplications(deviceApplications: List<DeviceApplication>) {
        this.deviceApplications.clear()
        this.deviceApplications.addAll(deviceApplications)
        syncList()
    }

    private fun syncList() {
        val list = ArrayList<Any>()
        deviceSpecs?.let { list.add(it) }
        list.add(deviceApplications)
        setItems(list)
        notifyDataSetChanged()
    }

    class DeviceSpecsAdapterDelegate : AbsListItemAdapterDelegate<Any, Any, DeviceSpecsViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is DeviceSpecs
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): DeviceSpecsViewHolder {
            val context = viewGroup.context
            val deviceSpecsCard = DeviceSpecsCard(context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            deviceSpecsCard.layoutParams = layoutParams
            return DeviceSpecsViewHolder(deviceSpecsCard)
        }

        override fun onBindViewHolder(
                model: Any, deviceSpecsViewHolder: DeviceSpecsViewHolder, list: List<Any>) {
            deviceSpecsViewHolder.bind(model as DeviceSpecs)
        }
    }

    class DeviceSpecsViewHolder(private val view: DeviceSpecsCard) : RecyclerView.ViewHolder(view) {
        fun bind(deviceSpecs: DeviceSpecs) {
            view.setDeviceSpecs(deviceSpecs)
        }
    }

    class DeviceApplicationsPreviewAdapterDelegate : AbsListItemAdapterDelegate<Any, Any, DeviceApplicationsPreviewViewHolder>() {

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

        override fun onBindViewHolder(
                model: Any, deviceSpecsViewHolder: DeviceApplicationsPreviewViewHolder, list: List<Any>) {
            deviceSpecsViewHolder.bind(model as List<DeviceApplication>)
        }
    }

    class DeviceApplicationsPreviewViewHolder(private val view: DeviceApplicationsPreviewCard) : RecyclerView.ViewHolder(view) {
        fun bind(deviceApplications: List<DeviceApplication>) {
            view.setDeviceApplications(deviceApplications)
        }
    }
}
