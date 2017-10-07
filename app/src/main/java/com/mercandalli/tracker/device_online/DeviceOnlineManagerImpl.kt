package com.mercandalli.tracker.device_online

import android.util.Log
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
        private val deviceApplicationManager: DeviceApplicationManager) : DeviceOnlineManager {

    companion object {
        private val DEVICE_REFERENCE_KEY: String = "device"

        private val DEVICE_SPECS_REFERENCE_KEY: String = "device-specs"
        private val DEVICE_APPS_REFERENCE_KEY: String = "device-apps"
    }

    private val deviceSpecs: DeviceSpecs = deviceSpecsManager.getDeviceSpecs()

    override fun initialize() {
        sendDeviceSpecs()
        sendDeviceApps()
        deviceApplicationManager.registerDeviceApplicationsListener(createDeviceApplicationsListener())
    }

    override fun getDeviceSpecs(deviceId: String) {
        /*
        firebaseDatabaseManager.getObjects(
                path,
                object : FirebaseDatabaseManager.OnGetObjectsListener {
                    override fun onGetObjectsFailed(e: Exception) {
                        Log.e("jm/debug", "getObjects failed", e)
                    }

                    override fun onGetObjectsSucceeded(objects: List<Any>) {
                        Log.d("jm/debug", "getObjects succeeded" + objects)
                        Log.d("jm/debug", "getObjects succeeded " + objects.size)
                        Log.d("jm/debug", "getObjects succeeded " + objects[0])
                    }
                })
                */
    }

    private fun sendDeviceSpecs() {
        val path = asList(DEVICE_REFERENCE_KEY, deviceSpecs.deviceId, DEVICE_SPECS_REFERENCE_KEY)
        firebaseDatabaseManager.putObject(
                path,
                deviceSpecs,
                object : FirebaseDatabaseManager.OnPutObjectListener {
                    override fun onPutObjectFailed(e: Exception) {
                        Log.e("jm/debug", "sendDeviceSpecs failed", e)
                    }

                    override fun onPutObjectSucceeded() {
                        Log.d("jm/debug", "sendDeviceSpecs succeeded")
                    }
                })
    }

    private fun sendDeviceApps() {
        val deviceApplications = deviceApplicationManager.getDeviceApplications()
        if (deviceApplications.isEmpty()) {
            return
        }
        val path = asList(DEVICE_REFERENCE_KEY, deviceSpecs.deviceId, DEVICE_APPS_REFERENCE_KEY)
        firebaseDatabaseManager.putObject(
                path,
                deviceApplications,
                object : FirebaseDatabaseManager.OnPutObjectListener {
                    override fun onPutObjectFailed(e: Exception) {
                        Log.e("jm/debug", "sendDeviceApps failed", e)
                    }

                    override fun onPutObjectSucceeded() {
                        Log.d("jm/debug", "sendDeviceApps succeeded")
                    }
                })
    }

    private fun createDeviceApplicationsListener(): DeviceApplicationManager.DeviceApplicationsListener {
        return object : DeviceApplicationManager.DeviceApplicationsListener {
            override fun onDeviceApplicationsChanged() {
                sendDeviceApps()
            }
        }
    }
}
