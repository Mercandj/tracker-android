package com.mercandalli.tracker.cloud_messaging

interface CloudMessagingIdManager {

    fun getCloudMessagingId()

    fun getCloudMessagingIdSync(): String?

    fun onCloudMessagingIdReceived(cloudMessagingId: String)

    fun registerCloudMessagingIdListener(listener: CloudMessagingIdListener)

    fun unregisterCloudMessagingIdListener(listener: CloudMessagingIdListener)

    interface CloudMessagingIdListener {

        fun onMessageIdReceived(cloudMessagingId: String)
    }
}
