package com.mercandalli.tracker.user

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mercandalli.tracker.device_specs.DeviceSpecsManager




internal class UserManagerImpl constructor(
        private val firebaseStorage: FirebaseStorage,
        private val userDeviceSpecsFileCreator: UserDeviceSpecsFileCreator,
        private val deviceSpecsManager: DeviceSpecsManager) : UserManager {

    private val usersReference: StorageReference

    init {
        val appReference = firebaseStorage.reference
        usersReference = appReference.child("user")
    }

    override fun sendDeviceSpecs() {
        val deviceSpecs = deviceSpecsManager.getDeviceSpecs()
        val userReference = usersReference.child(deviceSpecs.deviceId)
        val deviceSpecsReference = userReference.child("device-specs.json")
        val deviceSpecsByteArray = userDeviceSpecsFileCreator.toData(
                deviceSpecs)
        val uploadTask = deviceSpecsReference.putBytes(deviceSpecsByteArray)
        uploadTask.addOnFailureListener({ e ->
            run {
                Log.e("jm/debug", "sendDeviceSpecs failed", e)
            }
        }).addOnSuccessListener({ taskSnapshot ->
            getDeviceSpecs(deviceSpecs.deviceId)
        })
    }

    override fun getDeviceSpecs(userId: String) {
        val userReference = usersReference.child(userId)
        val deviceSpecsReference = userReference.child("device-specs.json")

        val ONE_MEGABYTE = (1024 * 1024).toLong()
        deviceSpecsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener({ byteArray ->
            run {
                val json = String(byteArray)
                Log.d("jm/debug", "getDeviceSpecs: " + json)
            }
        }).addOnFailureListener({
            Log.d("jm/debug", "getDeviceSpecs failed")
        })
    }
}