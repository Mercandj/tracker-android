package com.mercandalli.tracker.notification

import android.app.Notification

interface NotificationManager {

    fun showNotification(message: String)

    fun getNotification(message: String): Notification
}
