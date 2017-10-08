package com.mercandalli.tracker.device_online

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.mercandalli.tracker.cloud_messaging.CloudMessagingIdManager
import com.mercandalli.tracker.device.Device
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

        private val DEVICE_SPEC_REFERENCE_KEY: String = "deviceSpec"
        private val DEVICE_APPS_REFERENCE_KEY: String = "deviceApplications"
        private val DEVICE_TOKEN_REFERENCE_KEY: String = "deviceToken"
    }

    private val deviceSpec: DeviceSpec = deviceSpecsManager.getDeviceSpec()
    private val devices = ArrayList<Device>()
    private val onDeviceSpecsListeners = ArrayList<DeviceOnlineManager.OnDevicesListener>()

    override fun initialize() {
        sendDeviceSpec()
        sendDeviceApps()
        sendDeviceToken()
        deviceApplicationManager.registerDeviceApplicationsListener(createDeviceApplicationsListener())
        cloudMessagingIdManager.registerCloudMessagingIdListener(createCloudMessagingIdListener())
    }

    override fun getDevicesSync(): List<Device> {
        val path = asList(DEVICE_REFERENCE_KEY)
        if (devices.isEmpty()) {
            firebaseDatabaseManager.getObjects(
                    path,
                    object : FirebaseDatabaseManager.OnGetObjectsListener {
                        override fun onGetObjectsFailed(e: Exception) {
                            Log.e("jm/debug", "getDeviceSpec failed", e)
                        }

                        override fun onGetObjectsSucceeded(dataSnapshot: DataSnapshot) {
                            devices.clear()
                            dataSnapshot.children.mapNotNullTo(devices) {
                                it.getValue(Device.Response::class.java)?.toDevice()
                            }
                            notifyDeviceSpecsChanged()
                        }
                    })
        }
        return ArrayList(devices)
    }

    override fun registerDevicesListener(listener: DeviceOnlineManager.OnDevicesListener) {
        if (onDeviceSpecsListeners.contains(listener)) {
            return
        }
        onDeviceSpecsListeners.add(listener)
    }

    override fun unregisterDevicesListener(listener: DeviceOnlineManager.OnDevicesListener) {
        onDeviceSpecsListeners.remove(listener)
    }

    private fun notifyDeviceSpecsChanged() {
        for (listener in onDeviceSpecsListeners) {
            listener.onDevicesChanged()
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
        val path = asList(DEVICE_REFERENCE_KEY, deviceSpec.deviceId, DEVICE_APPS_REFERENCE_KEY)
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
