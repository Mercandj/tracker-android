package com.mercandalli.tracker.device

import android.content.SharedPreferences
import com.mercandalli.tracker.firebase.FirebaseDatabaseManager
import java.lang.Exception
import java.util.*

internal class DeviceNicknameManagerImpl constructor(
        private val deviceTrackId: String,
        private val sharedPreferences: SharedPreferences,
        private val firebaseDatabaseManager: FirebaseDatabaseManager) : DeviceNicknameManager {

    companion object {
        private val DEVICE_REFERENCE_KEY: String = "device"
        private val DEVICE_NICKNAME_REFERENCE_KEY: String = "deviceNickname"
    }

    private var deviceNickname: String? = null

    init {
        deviceNickname = sharedPreferences.getString("deviceNickname", null)
    }

    override fun getNickname(): String? {
        return deviceNickname
    }

    override fun setNickname(deviceNickname: String) {
        this.deviceNickname = deviceNickname
        val path = Arrays.asList(DEVICE_REFERENCE_KEY, deviceTrackId, DEVICE_NICKNAME_REFERENCE_KEY)
        firebaseDatabaseManager.putObject(
                path,
                deviceNickname,
                object : FirebaseDatabaseManager.OnPutObjectListener {
                    override fun onPutObjectSucceeded() {
                        sharedPreferences.edit().putString("deviceNickname", deviceNickname).apply()
                    }

                    override fun onPutObjectFailed(e: Exception) {

                    }
                }
        )
    }
}