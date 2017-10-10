package com.mercandalli.tracker.device_application

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Static methods related to the applications.
 */
object DeviceApplicationUtils {

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
     * @param onlyUserInstalledApp Only apps from manual installation.
     * `new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())`.
     */
    internal fun sortingNativeFromUserApp(
            packageManager: PackageManager,
            onlyUserInstalledApp: Boolean): List<DeviceApplicationInternal> {

        val apps = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        val userApps = ArrayList<PackageInfo>()
        val nativeApps = ArrayList<PackageInfo>()

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolvedInfos = packageManager.queryIntentActivities(mainIntent, 0)
        Collections.sort(resolvedInfos, ResolveInfo.DisplayNameComparator(packageManager))

        for (i in apps.indices.reversed()) {
            val packageInfo = apps[i]
            if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0 || packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0) {
                nativeApps.add(packageInfo)
            }

            resolvedInfos
                    .filter { packageInfo.packageName == it.activityInfo.applicationInfo.packageName }
                    .forEach { userApps.add(packageInfo) }
        }

        return createAppInstalledList(
                packageManager,
                nativeApps,
                userApps,
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
            packageManager: PackageManager,
            nativeApp: List<PackageInfo>,
            userApp: List<PackageInfo>,
            onlyUserInstalledApp: Boolean): List<DeviceApplicationInternal> {
        val list = ArrayList<DeviceApplicationInternal>()
        if (!onlyUserInstalledApp) {
            nativeApp.mapTo(list) {
                DeviceApplicationInternal(
                        DeviceApplication.PRE_INSTALL,
                        it.applicationInfo.loadLabel(packageManager).toString(),
                        it.packageName,
                        it.versionCode,
                        it.versionName,
                        it.firstInstallTime,
                        it.lastUpdateTime,
                        0,
                        0,
                        it.applicationInfo.loadIcon(packageManager),
                        it.applicationInfo.targetSdkVersion)
            }
        }
        userApp.mapTo(list) {
            DeviceApplicationInternal(
                    DeviceApplication.USER_INSTALL,
                    it.applicationInfo.loadLabel(packageManager).toString(),
                    it.packageName,
                    it.versionCode,
                    it.versionName,
                    it.firstInstallTime,
                    it.lastUpdateTime,
                    0,
                    0,
                    it.applicationInfo.loadIcon(packageManager),
                    it.applicationInfo.targetSdkVersion)
        }
        return list
    }
}
