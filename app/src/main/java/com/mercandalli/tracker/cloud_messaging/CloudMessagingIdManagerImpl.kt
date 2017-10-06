package com.mercandalli.tracker.cloud_messaging

import com.mercandalli.tracker.common.Preconditions
import com.mercandalli.tracker.main_thread.MainThreadPost
import java.util.*

internal class CloudMessagingIdManagerImpl(
        private val mainThreadPost: MainThreadPost,
        private val delegate: Delegate) : CloudMessagingIdManager {

    private val cloudMessagingIdListener = ArrayList<CloudMessagingIdManager.CloudMessagingIdListener>()
    private var cloudMessagingId: String? = null

    init {
        Preconditions.checkNotNull(mainThreadPost)
        Preconditions.checkNotNull(delegate)
    }

    override fun getCloudMessagingId() {
        delegate.startCloudMessagingIdIntentService()
    }

    override fun onCloudMessagingIdReceived(cloudMessagingId: String) {
        this.cloudMessagingId = cloudMessagingId
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { onCloudMessagingIdReceived(cloudMessagingId) })
            return
        }
        for (listener in cloudMessagingIdListener) {
            listener.onMessageIdReceived(cloudMessagingId)
        }
    }

    override fun registerCloudMessagingIdListener(
            listener: CloudMessagingIdManager.CloudMessagingIdListener) {
        if (cloudMessagingIdListener.contains(listener)) {
            cloudMessagingIdListener.remove(listener)
        }
        cloudMessagingIdListener.add(listener)
    }

    override fun unregisterCloudMessagingIdListener(
            listener: CloudMessagingIdManager.CloudMessagingIdListener) {
        cloudMessagingIdListener.remove(listener)
    }

    override fun getCloudMessagingIdSync(): String? {
        return cloudMessagingId
    }

    internal interface Delegate {
        fun startCloudMessagingIdIntentService()
    }
}
