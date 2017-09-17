package com.mercandalli.tracker.location

interface LocationManager {

    fun requestSingleUpdate()

    fun requestLocationUpdates()

    fun stopLocationUpdates()

    fun startLocationSetting()

    fun isLocationEnable(): Boolean

    fun registerLocationListener(listener: LocationListener)

    fun unregisterLocationListener(listener: LocationListener)

    interface LocationListener {

        fun onLocationChanged(location: Location)
    }
}
