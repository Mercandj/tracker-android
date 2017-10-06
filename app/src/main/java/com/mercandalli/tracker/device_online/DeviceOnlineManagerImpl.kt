package com.mercandalli.tracker.device_online

import android.util.Log
import com.mercandalli.tracker.device_specs.DeviceSpecsManager
import com.mercandalli.tracker.firebase.FirebaseDatabaseManager
import com.mercandalli.tracker.firebase.FirebaseStorageManager
import java.lang.Exception

internal class DeviceOnlineManagerImpl constructor(
        private val firebaseStorageManager: FirebaseStorageManager,
        private val firebaseDatabaseManager: FirebaseDatabaseManager,
        private val deviceSpecsConverter: DeviceSpecsConverter,
        private val deviceFamiliesConverter: DeviceFamiliesConverter,
        private val deviceSpecsManager: DeviceSpecsManager) : DeviceOnlineManager {

    companion object {
        private val DEVICE_REFERENCE_KEY: String = "device"
        private val DEVICE_FAMILY_REFERENCE_KEY: String = "device-family"
    }

    init {

    }

    override fun send() {
        val path = ArrayList<String>()
        path.add(DEVICE_REFERENCE_KEY)
        val deviceSpecs = deviceSpecsManager.getDeviceSpecs()
        firebaseDatabaseManager.putObject(
                path,
                deviceSpecs.deviceId,
                deviceSpecs,
                object : FirebaseDatabaseManager.OnPutObjectListener {
                    override fun onPutObjectFailed(e: Exception) {
                        Log.e("jm/debug", "putObject failed", e)
                    }

                    override fun onPutObjectSucceeded() {
                        Log.d("jm/debug", "putObject succeeded")
                    }
                })

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
    }

    override fun getDeviceSpecs(deviceId: String) {

    }
}