package com.mercandalli.tracker.cloud_messaging

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.mercandalli.tracker.main.MainApplication

class CloudMessagingIdIntentService : IntentService(NAME) {

    companion object {

        private val NAME = "CloudMessagingIdIntentService"

        fun start(context: Context) {
            val intent = Intent(context, CloudMessagingIdIntentService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = MainApplication.appComponent.provideNotificationManager()
            val test = notificationManager.getNotification("Test")
            startForeground(1, test)
        }

        try {
            val gcmTokenId = FirebaseInstanceId.getInstance().token
            if (gcmTokenId != null) {
                val appComponent = MainApplication.appComponent
                val cloudMessagingIdManager = appComponent.provideCloudMessagingIdManager()
                cloudMessagingIdManager.onCloudMessagingIdReceived(gcmTokenId)
            }
        } catch (e: Exception) {
            Log.e("jm/debug", "error", e)
        }

    }
}
