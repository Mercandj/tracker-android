package com.mercandalli.tracker.device

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.mercandalli.tracker.R
import com.mercandalli.tracker.device_specs.DeviceSpecs
import com.mercandalli.tracker.device_specs.DeviceSpecsCard

internal class DeviceRecyclerAdapter : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(DiscoverEntriesAdapterDelegate() as AdapterDelegate<List<Any>>)
    }

    fun setDeviceSpecs(deviceSpecs: DeviceSpecs) {
        val list = ArrayList<Any>()
        list.add(deviceSpecs)
        setItems(list)
        notifyDataSetChanged()
    }

    class DiscoverEntriesAdapterDelegate : AbsListItemAdapterDelegate<Any, Any, DiscoverEntriesHeaderViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is DeviceSpecs
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): DiscoverEntriesHeaderViewHolder {
            val context = viewGroup.context
            val discoverEntriesHeaderView = DeviceSpecsCard(context)

            val resources = context.resources
            val defaultSpace = resources.getDimensionPixelSize(R.dimen.default_space)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, defaultSpace, 0, defaultSpace)
            discoverEntriesHeaderView.layoutParams = layoutParams

            return DiscoverEntriesHeaderViewHolder(discoverEntriesHeaderView)
        }

        override fun onBindViewHolder(
                model: Any,
                discoverEntriesHeaderViewHolder: DiscoverEntriesHeaderViewHolder,
                list: List<Any>) {
            discoverEntriesHeaderViewHolder.bind(model as DeviceSpecs)
        }
    }

    class DiscoverEntriesHeaderViewHolder(
            private val view: DeviceSpecsCard) : RecyclerView.ViewHolder(view) {

        fun bind(deviceSpecs: DeviceSpecs) {
            view.setDeviceSpecs(deviceSpecs)
        }
    }
}
