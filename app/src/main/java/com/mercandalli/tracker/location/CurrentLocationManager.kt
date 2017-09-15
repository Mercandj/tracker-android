package com.mercandalli.tracker.location

interface CurrentLocationManager {

    fun requestSingleUpdate()

    fun cancelRequestLocationUpdates()

    fun isLocationEnable(): Boolean

    fun registerCurrentLocationListener(listener: CurrentLocationListener)

    fun unregisterCurrentLocationListener(listener: CurrentLocationListener)

    interface CurrentLocationListener {

        fun onCurrentLocationChanged(location: CurrentLocation?)
    }
}
