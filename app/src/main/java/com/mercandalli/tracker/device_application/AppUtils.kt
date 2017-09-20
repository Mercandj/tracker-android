package com.mercandalli.tracker.device_application

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Static methods related to the applications.
 */
object AppUtils {

    fun launchAppOrStore(
            context: Context,
            packageStr: String) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageStr)
        if (intent == null) {
            val intentMarket = Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + packageStr))
            intentMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(intentMarket)
            } catch (ignored: ActivityNotFoundException) {
            }

            return
        }
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        context.startActivity(intent)
    }

    fun isAppInstalled(
            context: Context,
            packageName: String): Boolean {
        val packageManager = context.packageManager
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

    }

    fun launchAppSettings(
            context: Context,
            appPackageName: String) {
        try {
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:" + appPackageName)
            context.startActivity(intent)
            Toast.makeText(context, "Settings : " + appPackageName, Toast.LENGTH_SHORT).show()
        } catch (ex: ActivityNotFoundException) {
            val intent = Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            context.startActivity(intent)
        }

    }

    /**
     * Dispatch native package into {#mPackageInfoInstalledSystemList} and lettings user package
     * into {#mPackageInfoList}.
     *
     * @param packageManager   A simple [PackageManager].
     * @param simpleDateFormat A simple [SimpleDateFormat] like
     * `new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())`.
     */
    fun sortingNativeFromUserApp(
            packageManager: PackageManager,
            onlyUserInstalledApp: Boolean): List<DeviceApplication> {

        val packageInfoList = packageManager.getInstalledPackages(
                PackageManager.GET_META_DATA or PackageManager.GET_UNINSTALLED_PACKAGES)
        val packageInfoInstalledSystemList = ArrayList<PackageInfo>()

        for (i in packageInfoList.indices.reversed()) {
            val packageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) == null) {
                packageInfoList.remove(packageInfo)
            }
            if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0 || packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0) {
                packageInfoInstalledSystemList.add(packageInfo)
                packageInfoList.remove(packageInfo)
            }
        }
        return createAppInstalledList(
                packageInfoInstalledSystemList,
                packageInfoList,
                onlyUserInstalledApp)
    }

    /**
     * Create list of [DeviceApplication] from list of PackageInfo.
     *
     * @param nativeApp        List of constructor package/.
     * @param userApp          List of package installed by user.
     * @param simpleDateFormat A simple [SimpleDateFormat] like
     * `new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())`.
     * @return A list of [DeviceApplication].
     */
    private fun createAppInstalledList(
            nativeApp: List<PackageInfo>,
            userApp: List<PackageInfo>,
            onlyUserInstalledApp: Boolean): List<DeviceApplication> {
        val list = ArrayList<DeviceApplication>()
        if (!onlyUserInstalledApp) {
            for (packageInfo in nativeApp) {
                list.add(DeviceApplication(
                        DeviceApplication.PRE_INSTALL,
                        packageInfo.applicationInfo.name,
                        packageInfo.packageName,
                        packageInfo.versionCode,
                        packageInfo.versionName,
                        packageInfo.firstInstallTime,
                        packageInfo.lastUpdateTime,
                        0))
            }
        }
        for (packageInfo in userApp) {
            if (packageInfo.applicationInfo.name != null) {
                list.add(DeviceApplication(
                        DeviceApplication.USER_INSTALL,
                        packageInfo.applicationInfo.name,
                        packageInfo.packageName,
                        packageInfo.versionCode,
                        packageInfo.versionName,
                        packageInfo.firstInstallTime,
                        packageInfo.lastUpdateTime,
                        0))
            }
        }
        return list
    }
}// Non-instantiable.
