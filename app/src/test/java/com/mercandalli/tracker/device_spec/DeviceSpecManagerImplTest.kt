package com.mercandalli.tracker.device_spec

import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Test class for the [DeviceSpecManagerImpl]
 */
class DeviceSpecManagerImplTest {

    private var delegate: DeviceSpecManagerImpl.Delegate = mock(DeviceSpecManagerImpl.Delegate::class.java)

    @Before
    fun setup() {
        `when`(delegate.getDeviceManufacturer()).thenReturn(DeviceSpecTest.DEVICE_MANUFACTURER_DEFAULT)
        `when`(delegate.getDeviceModel()).thenReturn(DeviceSpecTest.DEVICE_MODEL_DEFAULT)
        `when`(delegate.getDeviceHardware()).thenReturn(DeviceSpecTest.DEVICE_HARDWARE_DEFAULT)
        `when`(delegate.getDeviceOsVersion()).thenReturn(DeviceSpecTest.DEVICE_OS_VERSION_DEFAULT)
    }

    @Test
    fun deviceRooted() {
        val deviceSpecManagerImpl = DeviceSpecManagerImpl(
                DeviceSpecTest.DEVICE_ID_DEFAULT,
                DeviceSpecTest.DEVICE_DENSITY_DEFAULT,
                DeviceSpecTest.DEVICE_IS_EMULATOR_DEFAULT,
                true,
                delegate)
        Assert.assertTrue(deviceSpecManagerImpl.getDeviceSpec().deviceRooted)
    }

    @Test
    fun deviceNotRooted() {
        val deviceSpecManagerImpl = DeviceSpecManagerImpl(
                DeviceSpecTest.DEVICE_ID_DEFAULT,
                DeviceSpecTest.DEVICE_DENSITY_DEFAULT,
                DeviceSpecTest.DEVICE_IS_EMULATOR_DEFAULT,
                false,
                delegate)
        Assert.assertFalse(deviceSpecManagerImpl.getDeviceSpec().deviceRooted)
    }

    @Test
    fun deviceEmulator() {
        val deviceSpecManagerImpl = DeviceSpecManagerImpl(
                DeviceSpecTest.DEVICE_ID_DEFAULT,
                DeviceSpecTest.DEVICE_DENSITY_DEFAULT,
                true,
                DeviceSpecTest.DEVICE_IS_ROOTED_DEFAULT,
                delegate)
        Assert.assertTrue(deviceSpecManagerImpl.getDeviceSpec().deviceEmulator)
    }

    @Test
    fun deviceNotEmulator() {
        val deviceSpecManagerImpl = DeviceSpecManagerImpl(
                DeviceSpecTest.DEVICE_ID_DEFAULT,
                DeviceSpecTest.DEVICE_DENSITY_DEFAULT,
                false,
                DeviceSpecTest.DEVICE_IS_ROOTED_DEFAULT,
                delegate)
        Assert.assertFalse(deviceSpecManagerImpl.getDeviceSpec().deviceEmulator)
    }
}