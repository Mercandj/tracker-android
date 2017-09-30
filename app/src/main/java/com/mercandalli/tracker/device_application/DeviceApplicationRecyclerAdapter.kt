package com.mercandalli.tracker.device_application

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

internal class DeviceApplicationRecyclerAdapter : ListDelegationAdapter<List<Any>>() {

    private var deviceApplications = ArrayList<DeviceApplication>()

    init {
        delegatesManager.addDelegate(DeviceApplicationAdapterDelegate() as AdapterDelegate<List<Any>>)
    }

    fun setDeviceApplications(deviceApplications: List<DeviceApplication>) {
        this.deviceApplications.clear()
        this.deviceApplications.addAll(deviceApplications)
        syncList()
    }

    private fun syncList() {
        val list = ArrayList<Any>()
        list.addAll(deviceApplications)
        setItems(list)
        notifyDataSetChanged()
    }

    private class DeviceApplicationAdapterDelegate :
            AbsListItemAdapterDelegate<Any, Any, DeviceApplicationViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is DeviceApplication
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): DeviceApplicationViewHolder {
            val context = viewGroup.context
            val deviceApplicationCard = DeviceApplicationCard(context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            deviceApplicationCard.layoutParams = layoutParams
            return DeviceApplicationViewHolder(deviceApplicationCard)
        }

        override fun onBindViewHolder(
                model: Any, deviceApplicationViewHolder: DeviceApplicationViewHolder, list: List<Any>) {
            deviceApplicationViewHolder.bind(model as DeviceApplication)
        }
    }

    private class DeviceApplicationViewHolder(
            private val view: DeviceApplicationCard) :
            RecyclerView.ViewHolder(view) {
        fun bind(deviceApplication: DeviceApplication) {
            view.setDeviceApplication(deviceApplication)
        }
    }
}