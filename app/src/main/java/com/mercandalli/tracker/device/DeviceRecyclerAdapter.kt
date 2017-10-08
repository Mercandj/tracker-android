package com.mercandalli.tracker.device

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.mercandalli.tracker.R
import com.mercandalli.tracker.device_application.DeviceApplication
import com.mercandalli.tracker.device_application.DeviceApplicationsPreviewCard
import com.mercandalli.tracker.device_spec.DeviceSpec
import com.mercandalli.tracker.device_spec.DeviceSpecCard

internal class DeviceRecyclerAdapter : ListDelegationAdapter<List<Any>>() {

    private var title: String? = null
    private var deviceSpec: DeviceSpec? = null
    private var deviceSpecs = ArrayList<DeviceSpec>()
    private var deviceApplications = ArrayList<DeviceApplication>()
    private var setDeviceApplicationCalled = false

    init {
        delegatesManager.addDelegate(TitleAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(DeviceSpecAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(DeviceApplicationsPreviewAdapterDelegate() as AdapterDelegate<List<Any>>)
    }

    fun setTitle(title: String) {
        this.title = title
        syncList()
    }

    fun setDeviceSpec(deviceSpec: DeviceSpec) {
        this.deviceSpec = deviceSpec
        syncList()
    }

    fun setDeviceApplications(deviceApplications: List<DeviceApplication>) {
        setDeviceApplicationCalled = true
        this.deviceApplications.clear()
        this.deviceApplications.addAll(deviceApplications)
        syncList()
    }

    fun setObjects(listParam: List<Any>) {
        val list = ArrayList<Any>(listParam)
        setItems(list)
        notifyDataSetChanged()
    }

    private fun syncList() {
        val list = ArrayList<Any>()
        if (title != null) {
            list.add(title!!)
        }
        list.addAll(deviceSpecs)
        deviceSpec?.let { list.add(it) }
        if (setDeviceApplicationCalled) {
            list.add(deviceApplications)
        }
        setItems(list)
        notifyDataSetChanged()
    }

    //region Title
    private class TitleAdapterDelegate :
            AbsListItemAdapterDelegate<Any, Any, TitleViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is String
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): TitleViewHolder {
            val context = viewGroup.context
            val textView = TextView(context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            val marginHorizontal = context.resources.getDimensionPixelSize(R.dimen.default_space_1_5)
            val marginTop = context.resources.getDimensionPixelSize(R.dimen.default_space_2)
            val marginBottom = 0
            layoutParams.setMargins(marginHorizontal, marginTop, marginHorizontal, marginBottom)
            textView.layoutParams = layoutParams
            textView.setTextColor(ContextCompat.getColor(context, R.color.color_text_title_1))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_xxl))
            textView.setTypeface(textView.typeface, Typeface.BOLD)
            return TitleViewHolder(textView)
        }

        override fun onBindViewHolder(
                model: Any, titleViewHolder: TitleViewHolder, list: List<Any>) {
            titleViewHolder.bind(model as String)
        }
    }

    private class TitleViewHolder(
            private val view: TextView) :
            RecyclerView.ViewHolder(view) {
        fun bind(o: String) {
            view.text = o
        }
    }
    //endregion Title

    //region DeviceSpec
    private class DeviceSpecAdapterDelegate :
            AbsListItemAdapterDelegate<Any, Any, DeviceSpecViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is DeviceSpec
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): DeviceSpecViewHolder {
            val context = viewGroup.context
            val deviceSpecCard = DeviceSpecCard(context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            deviceSpecCard.layoutParams = layoutParams
            return DeviceSpecViewHolder(deviceSpecCard)
        }

        override fun onBindViewHolder(
                model: Any, deviceSpecViewHolder: DeviceSpecViewHolder, list: List<Any>) {
            deviceSpecViewHolder.bind(model as DeviceSpec)
        }
    }

    private class DeviceSpecViewHolder(
            private val view: DeviceSpecCard) :
            RecyclerView.ViewHolder(view) {
        fun bind(deviceSpec: DeviceSpec) {
            view.setDeviceSpecs(deviceSpec)
        }
    }
    //endregion DeviceSpec

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
