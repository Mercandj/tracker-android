package com.mercandalli.tracker.push

interface PushSenderManager {

    fun sendPush(cloudMessagingId: String, message: String)
}
