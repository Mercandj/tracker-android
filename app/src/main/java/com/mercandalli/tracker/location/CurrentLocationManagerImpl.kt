package com.mercandalli.tracker.location

import android.annotation.SuppressLint
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.mercandalli.tracker.main_thread.MainThreadPost
import java.util.*

internal class CurrentLocationManagerImpl(
        private val locationManager: LocationManager,
        private val mainThreadPost: MainThreadPost) : CurrentLocationManager {

    private val locationListener = createLocationListener()
    private val currentLocationListeners = ArrayList<CurrentLocationManager.CurrentLocationListener>()
    private val criteria: Criteria = Criteria()

    init {
        // Now first make a criteria with your requirements
        // this is done to save the battery life of the device
        // there are various other other criteria you can search for..
        criteria.accuracy = Criteria.ACCURACY_COARSE
        criteria.powerRequirement = Criteria.POWER_LOW
        criteria.isAltitudeRequired = false
        criteria.isBearingRequired = false
        criteria.isSpeedRequired = false
        criteria.isCostAllowed = true
        criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
        criteria.verticalAccuracy = Criteria.ACCURACY_HIGH
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation() {
        if (isLocationEnable()) {
            locationManager.requestSingleUpdate(criteria, locationListener, null)
        } else {
            notifyCurrentLocation(null)
        }
    }

    override fun cancelRequestLocationUpdates() {
        locationManager.removeUpdates(locationListener)
    }

    override fun isLocationEnable(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun registerCurrentLocationListener(listener: CurrentLocationManager.CurrentLocationListener) {
        if (currentLocationListeners.contains(listener)) {
            return
        }
        currentLocationListeners.add(listener)
    }

    override fun unregisterCurrentLocationListener(listener: CurrentLocationManager.CurrentLocationListener) {
        currentLocationListeners.remove(listener)
    }

    private fun notifyCurrentLocation(location: CurrentLocation?) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyCurrentLocation(location) })
            return
        }
        for (listener in currentLocationListeners) {
            listener.onCurrentLocationChanged(location)
        }
    }

    private fun createLocationListener(): LocationListener {
        return object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val currentLocation = CurrentLocation.create(location)
                notifyCurrentLocation(currentLocation)
                locationManager.removeUpdates(this)
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}

            override fun onProviderEnabled(s: String) {}

            override fun onProviderDisabled(s: String) {}
        }
    }
}
