package com.mercandalli.tracker.device_application

import android.graphics.drawable.Drawable
import android.support.annotation.StringDef
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model of an app installed with some information.
 */
class DeviceApplication internal constructor(

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

        @Expose
        val icon: Drawable) {

    @Retention
    @StringDef(PRE_INSTALL, USER_INSTALL)
    annotation class KindInstallation

    override fun toString(): String {
        return "DeviceApplication{" +
                "kindInstallation='" + kindInstallation + '\'' +
                ", androidAppName='" + androidAppName + '\'' +
                ", androidAppPackage='" + `package` + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", installedAt='" + installedAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", totalTimeInForeground='" + totalTimeInForeground + '\'' +
                ", lastLaunch='" + lastLaunch + '\'' +
                ", icon='" + icon + '\'' +
                '}'
    }

    companion object {

        const val PRE_INSTALL = "preinstall"

        const val USER_INSTALL = "user_install"
    }
}
