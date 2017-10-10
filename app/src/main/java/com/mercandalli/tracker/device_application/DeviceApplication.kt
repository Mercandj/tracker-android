package com.mercandalli.tracker.device_application

import android.support.annotation.StringDef
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

/**
 * Model of an app installed with some information.
 */
@IgnoreExtraProperties
data class DeviceApplication(
        @SerializedName("device-tracker-id")
        val deviceTrackerId: String,

        @KindInstallation
        @SerializedName("kind_installation")
        val kindInstallation: String,

        @SerializedName("android_app_name")
        val androidAppName: String,

        @SerializedName("android_app_package")
        val `package`: String,

        @SerializedName("android_app_version_code")
        val versionCode: Int,

        @SerializedName("android_app_version_name")
        val versionName: String,

        @SerializedName("installed_at")
        val installedAt: Long,

        @SerializedName("updated_at")
        val updatedAt: Long,

        @SerializedName("total_time_in_foreground")
        val totalTimeInForeground: Long,

        @SerializedName("last_launch")
        val lastLaunch: Long,

        @SerializedName("target_sdk_version")
        val targetSdkVersion: Int) {

    @Retention
    @StringDef(PRE_INSTALL, USER_INSTALL)
    annotation class KindInstallation

    companion object {

        const val PRE_INSTALL = "preinstall"

        const val USER_INSTALL = "user_install"
    }

    data class Response(
            var deviceTrackerId: String = "",
            var kindInstallation: String = "",
            var androidAppName: String = "",
            var `package`: String = "",
            var versionCode: Int = 0,
            var versionName: String = "",
            val installedAt: Long = 0,
            val updatedAt: Long = 0,
            val totalTimeInForeground: Long = 0,
            val lastLaunch: Long = 0,
            val targetSdkVersion: Int = 0) {

        fun toDeviceApplication(): DeviceApplication {
            return DeviceApplication(
                    deviceTrackerId,
                    kindInstallation,
                    androidAppName,
                    `package`,
                    versionCode,
                    versionName,
                    installedAt,
                    updatedAt,
                    totalTimeInForeground,
                    lastLaunch,
                    targetSdkVersion)
        }
    }
}
