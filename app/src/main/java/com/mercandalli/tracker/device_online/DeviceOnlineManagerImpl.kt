package com.mercandalli.tracker.device_online

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.mercandalli.tracker.cloud_messaging.CloudMessagingIdManager
import com.mercandalli.tracker.device_application.DeviceApplicationManager
import com.mercandalli.tracker.device_specs.DeviceSpecs
import com.mercandalli.tracker.device_specs.DeviceSpecsManager
import com.mercandalli.tracker.firebase.FirebaseDatabaseManager
import com.mercandalli.tracker.firebase.FirebaseStorageManager
import java.lang.Exception
import java.util.Arrays.asList

internal class DeviceOnlineManagerImpl constructor(
        private val firebaseStorageManager: FirebaseStorageManager,
        private val firebaseDatabaseManager: FirebaseDatabaseManager,
        private val deviceSpecsManager: DeviceSpecsManager,
        private val deviceApplicationManager: DeviceApplicationManager,
        private val cloudMessagingIdManager: CloudMessagingIdManager) : DeviceOnlineManager {

    companion object {
        private val DEVICE_REFERENCE_KEY: String = "device"

        private val DEVICE_SPECS_REFERENCE_KEY: String = "device-specs"
        private val DEVICE_APPS_REFERENCE_KEY: String = "device-apps"
        private val DEVICE_TOKEN_REFERENCE_KEY: String = "device-token"
    }

    private val deviceSpecs: DeviceSpecs = deviceSpecsManager.getDeviceSpecs()

    override fun initialize() {
        sendDeviceSpecs()
        sendDeviceApps()
        sendDeviceToken()
        deviceApplicationManager.registerDeviceApplicationsListener(createDeviceApplicationsListener())
        cloudMessagingIdManager.registerCloudMessagingIdListener(createCloudMessagingIdListener())

        getDeviceSpecs()
    }

    override fun getDeviceSpecs() {
        val path = asList(DEVICE_REFERENCE_KEY)
        firebaseDatabaseManager.getObjects(
                path,
                object : FirebaseDatabaseManager.OnGetObjectsListener {
                    override fun onGetObjectsFailed(e: Exception) {
                        Log.e("jm/debug", "getDeviceSpecs failed", e)
                    }

                    override fun onGetObjectsSucceeded(dataSnapshot: DataSnapshot) {
                        for (device in dataSnapshot.children) {
                            val deviceSpecs = device
                                    .child(DEVICE_SPECS_REFERENCE_KEY)
                                    .getValue(DeviceSpecsResponse::class.java)
                                    ?.toDeviceSpecs()
                            Log.d("jm/debug", "getDeviceSpecs succeeded device:" + deviceSpecs)
                        }
                    }
                })
    }

    private fun sendDeviceSpecs() {
        val path = asList(DEVICE_REFERENCE_KEY, deviceSpecs.deviceId, DEVICE_SPECS_REFERENCE_KEY)
        firebaseDatabaseManager.putObject(path, deviceSpecs, null)
    }

    private fun sendDeviceApps() {
        val deviceApplications = deviceApplicationManager.getDeviceApplications()
        if (deviceApplications.isEmpty()) {
            return
        }
        val path = asList(DEVICE_REFERENCE_KEY, deviceSpecs.deviceId, DEVICE_APPS_REFERENCE_KEY)
        firebaseDatabaseManager.putObject(path, deviceApplications, null)
    }

    private fun sendDeviceToken() {
        val deviceToken = cloudMessagingIdManager.getCloudMessagingIdSync() ?: return
        val path = asList(DEVICE_REFERENCE_KEY, deviceSpecs.deviceId, DEVICE_TOKEN_REFERENCE_KEY)
        firebaseDatabaseManager.putObject(path, deviceToken, null)
    }

    private fun createDeviceApplicationsListener(): DeviceApplicationManager.DeviceApplicationsListener {
        return object : DeviceApplicationManager.DeviceApplicationsListener {
            override fun onDeviceApplicationsChanged() {
                sendDeviceApps()
            }
        }
    }

    private fun createCloudMessagingIdListener(): CloudMessagingIdManager.CloudMessagingIdListener {
        return object : CloudMessagingIdManager.CloudMessagingIdListener {
            override fun onMessageIdReceived(cloudMessagingId: String) {
                sendDeviceToken()
            }
        }
    }
}
