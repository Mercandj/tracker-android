package com.mercandalli.tracker.device_online

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.mercandalli.tracker.cloud_messaging.CloudMessagingIdManager
import com.mercandalli.tracker.device_application.DeviceApplicationManager
import com.mercandalli.tracker.device_spec.DeviceSpec
import com.mercandalli.tracker.device_spec.DeviceSpecsManager
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

        private val DEVICE_SPEC_REFERENCE_KEY: String = "device-spec"
        private val DEVICE_APP_REFERENCE_KEY: String = "device-app"
        private val DEVICE_TOKEN_REFERENCE_KEY: String = "device-token"
    }

    private val deviceSpec: DeviceSpec = deviceSpecsManager.getDeviceSpec()
    private val deviceSpecsList = ArrayList<DeviceSpec>()
    private val onDeviceSpecsListeners = ArrayList<DeviceOnlineManager.OnDeviceSpecsListener>()

    override fun initialize() {
        sendDeviceSpec()
        sendDeviceApps()
        sendDeviceToken()
        deviceApplicationManager.registerDeviceApplicationsListener(createDeviceApplicationsListener())
        cloudMessagingIdManager.registerCloudMessagingIdListener(createCloudMessagingIdListener())
    }

    override fun getDeviceSpecsSync(): List<DeviceSpec> {
        val path = asList(DEVICE_REFERENCE_KEY)
        if (deviceSpecsList.isEmpty()) {
            firebaseDatabaseManager.getObjects(
                    path,
                    object : FirebaseDatabaseManager.OnGetObjectsListener {
                        override fun onGetObjectsFailed(e: Exception) {
                            Log.e("jm/debug", "getDeviceSpec failed", e)
                        }

                        override fun onGetObjectsSucceeded(dataSnapshot: DataSnapshot) {
                            deviceSpecsList.clear()
                            dataSnapshot.children
                                    .map {
                                        it
                                                .child(DEVICE_SPEC_REFERENCE_KEY)
                                                .getValue(DeviceSpecResponse::class.java)
                                                ?.toDeviceSpecs()
                                    }
                                    .forEach { deviceSpecsList.add(deviceSpec) }
                            notifyDeviceSpecsChanged()
                        }
                    })
        }
        return ArrayList(deviceSpecsList)
    }

    override fun registerDeviceSpecsListener(listener: DeviceOnlineManager.OnDeviceSpecsListener) {
        if (onDeviceSpecsListeners.contains(listener)) {
            return
        }
        onDeviceSpecsListeners.add(listener)
    }

    override fun unregisterDeviceSpecsListener(listener: DeviceOnlineManager.OnDeviceSpecsListener) {
        onDeviceSpecsListeners.remove(listener)
    }

    private fun notifyDeviceSpecsChanged() {
        for (listener in onDeviceSpecsListeners) {
            listener.onDeviceSpecsChanged()
        }
    }

    private fun sendDeviceSpec() {
        val path = asList(DEVICE_REFERENCE_KEY, deviceSpec.deviceId, DEVICE_SPEC_REFERENCE_KEY)
        firebaseDatabaseManager.putObject(path, deviceSpec, null)
    }

    private fun sendDeviceApps() {
        val deviceApplications = deviceApplicationManager.getDeviceApplications()
        if (deviceApplications.isEmpty()) {
            return
        }
        val path = asList(DEVICE_REFERENCE_KEY, deviceSpec.deviceId, DEVICE_APP_REFERENCE_KEY)
        firebaseDatabaseManager.putObject(path, deviceApplications, null)
    }

    private fun sendDeviceToken() {
        val deviceToken = cloudMessagingIdManager.getCloudMessagingIdSync() ?: return
        val path = asList(DEVICE_REFERENCE_KEY, deviceSpec.deviceId, DEVICE_TOKEN_REFERENCE_KEY)
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
