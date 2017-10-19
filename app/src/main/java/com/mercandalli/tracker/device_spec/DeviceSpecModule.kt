package com.mercandalli.tracker.device_spec

import android.os.Build
import com.mercandalli.tracker.R
import com.mercandalli.tracker.main.MainApplication
import com.mercandalli.tracker.root.RootManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceSpecModule {

    @Singleton
    @Provides
    fun provideDeviceSpecsManager(
            application: MainApplication,
            rootManager: RootManager): DeviceSpecManager {
        val deviceId = DeviceSpecUtils.getDeviceId(application)
        val deviceDensity = application.getString(R.string.device_spec_density)
        val deviceEmulator = DeviceSpecUtils.isEmulator()
        val deviceRooted = rootManager.isRooted()
        val delegate: DeviceSpecManagerImpl.Delegate = object : DeviceSpecManagerImpl.Delegate {

            override fun getDeviceManufacturer(): String {
                return Build.MANUFACTURER
            }

            override fun getDeviceModel(): String {
                return Build.MODEL
            }

            override fun getDeviceHardware(): String {
                return Build.HARDWARE
            }

            override fun getDeviceOsVersion(): Int {
                return Build.VERSION.SDK_INT
            }

            override fun getDeviceMacAddress(): String? {
                return DeviceSpecUtils.getMacAddress(application)
            }

            override fun getDeviceBatteryPercent(): Float {
                return DeviceSpecUtils.getBatteryPercent(application)
            }
        }
        return DeviceSpecManagerImpl(
                deviceId,
                deviceDensity,
                deviceEmulator,
                deviceRooted,
                delegate)
    }
}