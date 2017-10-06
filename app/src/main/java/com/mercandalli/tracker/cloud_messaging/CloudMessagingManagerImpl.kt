package com.mercandalli.tracker.cloud_messaging

import com.mercandalli.tracker.common.Preconditions
import com.mercandalli.tracker.push.PushSenderManager
import java.util.*

internal class CloudMessagingManagerImpl(
        private val pushSenderManager: PushSenderManager) : CloudMessagingManager {

    private val receptionMessageListeners = ArrayList<CloudMessagingManager
    .ReceptionMessageListener>()

    init {
        Preconditions.checkNotNull(pushSenderManager)
    }

    override fun sendMessage(cloudMessagingId: String, message: String) {
        pushSenderManager.sendPush(cloudMessagingId, message)
    }

    override fun onMessageReceived(message: String) {
        for (listener in receptionMessageListeners) {
            listener.onMessageReceived(message)
        }
    }

    override fun registerReceptionMessageListener(
            listener: CloudMessagingManager.ReceptionMessageListener) {
        if (receptionMessageListeners.contains(listener)) {
            return
        }
        receptionMessageListeners.add(listener)
    }

    override fun unregisterReceptionMessageListener(
            listener: CloudMessagingManager.ReceptionMessageListener) {
        receptionMessageListeners.remove(listener)
    }
}
