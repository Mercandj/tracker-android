package com.mercandalli.tracker.cloud_messaging

interface CloudMessagingManager {

    fun sendMessage(cloudMessagingId: String, message: String)

    fun onMessageReceived(message: String)

    fun registerReceptionMessageListener(listener: ReceptionMessageListener)

    fun unregisterReceptionMessageListener(listener: ReceptionMessageListener)

    interface ReceptionMessageListener {

        fun onMessageReceived(message: String)
    }
}
