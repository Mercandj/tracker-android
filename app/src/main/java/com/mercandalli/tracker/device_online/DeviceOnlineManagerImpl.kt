package com.mercandalli.tracker.device_online

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mercandalli.tracker.device_specs.DeviceSpecsManager

internal class DeviceOnlineManagerImpl constructor(
        private val firebaseStorage: FirebaseStorage,
        private val deviceSpecsConverter: DeviceSpecsConverter,
        private val deviceFamiliesConverter: DeviceFamiliesConverter,
        private val deviceSpecsManager: DeviceSpecsManager) : DeviceOnlineManager {

    companion object {
        private val DEVICE_REFERENCE_KEY: String = "device"
        private val DEVICE_DEVICE_SPECS_JSON_REFERENCE_KEY: String = "device-specs.json"
        private val DEVICE_FAMILY_REFERENCE_KEY: String = "device-family"
        private val DEVICE_FAMILY_JSON_REFERENCE_KEY: String = "device-families.json"
        private val ONE_MEGABYTE = (1024 * 1024).toLong()
    }

    private val deviceReference: StorageReference
    private val deviceFamilyReference: StorageReference

    init {
        val appReference = firebaseStorage.reference
        deviceReference = appReference.child(DEVICE_REFERENCE_KEY)
        deviceFamilyReference = appReference.child(DEVICE_FAMILY_REFERENCE_KEY)
    }

    override fun send() {
        sendDeviceSpecs()
        syncDeviceFamilies()
    }

    override fun getDeviceSpecs(deviceId: String) {
        val deviceUserFolderReference = deviceReference.child(deviceId)
        val deviceSpecsReference = deviceUserFolderReference.child(DEVICE_DEVICE_SPECS_JSON_REFERENCE_KEY)
        deviceSpecsReference.getBytes(ONE_MEGABYTE)
                .addOnFailureListener({ e ->
                    run {
                        Log.e("jm/debug", "getDeviceSpecs: failed", e)
                    }
                })
                .addOnSuccessListener({ byteArray ->
                    run {
                        val json = String(byteArray)
                        Log.d("jm/debug", "getDeviceSpecs: " + json)
                    }
                })
    }

    private fun sendDeviceSpecs() {
        val deviceSpecs = deviceSpecsManager.getDeviceSpecs()
        val userReference = deviceReference.child(deviceSpecs.deviceId)
        val deviceSpecsReference = userReference.child(DEVICE_DEVICE_SPECS_JSON_REFERENCE_KEY)
        val deviceSpecsByteArray = deviceSpecsConverter.toByteArray(deviceSpecs)
        val uploadTask = deviceSpecsReference.putBytes(deviceSpecsByteArray)
        uploadTask
                .addOnFailureListener({ e ->
                    run {
                        Log.e("jm/debug", "sendDeviceSpecs: failed", e)
                    }
                })
                .addOnSuccessListener({ taskSnapshot ->

                })
    }

    private fun syncDeviceFamilies() {
        val deviceFamiliesJsonReference = deviceFamilyReference.child(DEVICE_FAMILY_JSON_REFERENCE_KEY)
        deviceFamiliesJsonReference.getBytes(ONE_MEGABYTE)
                .addOnFailureListener({ e ->
                    run {
                        Log.e("jm/debug", "syncDeviceFamilies: failed", e)
                    }
                })
                .addOnSuccessListener({ byteArray ->
                    run {
                        val json = String(byteArray)
                        val deviceFamilies = deviceFamiliesConverter.toDeviceFamilies(json)
                        syncDeviceFamilies(deviceFamilies)
                    }
                })
    }

    private fun syncDeviceFamilies(deviceFamilies: DeviceFamilies) {

    }

    private fun sendDeviceFamilies(deviceFamilies: DeviceFamilies) {
        val deviceFamiliesJsonReference = deviceFamilyReference.child(DEVICE_FAMILY_JSON_REFERENCE_KEY)
        val byteArray = deviceFamiliesConverter.toByteArray(deviceFamilies)
        val uploadTask = deviceFamiliesJsonReference.putBytes(byteArray)
        uploadTask
                .addOnFailureListener({ e ->
                    run {
                        Log.e("jm/debug", "sendDeviceFamilies: failed", e)
                    }
                })
                .addOnSuccessListener({ taskSnapshot ->

                })
    }
}