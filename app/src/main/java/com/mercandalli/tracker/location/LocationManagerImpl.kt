package com.mercandalli.tracker.location

import android.annotation.SuppressLint
import android.location.Criteria
import android.location.LocationListener
import android.os.Bundle
import com.mercandalli.tracker.main_thread.MainThreadPost
import java.util.*

internal class LocationManagerImpl(
        private val locationManager: android.location.LocationManager,
        private val locationRepository: LocationRepository,
        private val mainThreadPost: MainThreadPost) : LocationManager {

    private val locationListener = createLocationListener()
    private val locationListeners = ArrayList<LocationManager.LocationListener>()
    private val criteria: Criteria = Criteria()
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES_METER: Float = 0F
    private val MIN_TIME_BW_UPDATES_MS = 0L

    init {
        criteria.accuracy = Criteria.ACCURACY_COARSE
        criteria.powerRequirement = Criteria.POWER_LOW
        criteria.isAltitudeRequired = true
        criteria.isBearingRequired = false
        criteria.isSpeedRequired = false
        criteria.isCostAllowed = true
        criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
        criteria.verticalAccuracy = Criteria.ACCURACY_HIGH
        criteria.speedAccuracy = Criteria.ACCURACY_HIGH
    }

    @SuppressLint("MissingPermission")
    override fun requestSingleUpdate() {
        if (isLocationEnable()) {
            locationManager.requestSingleUpdate(criteria, locationListener, null)
        }
    }

    @SuppressLint("MissingPermission")
    override fun requestLocationUpdates() {
        if (isLocationEnable()) {
            val lastKnownLocation = locationManager.getLastKnownLocation(
                    android.location.LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                onNewLocation(lastKnownLocation)
            }
            locationManager.requestLocationUpdates(
                    android.location.LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES_MS,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES_METER,
                    locationListener)
        }
    }

    override fun stopLocationUpdates() {
        locationManager.removeUpdates(locationListener)
    }

    override fun isLocationEnable(): Boolean {
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
    }

    override fun registerLocationListener(listener: LocationManager.LocationListener) {
        if (locationListeners.contains(listener)) {
            return
        }
        locationListeners.add(listener)
    }

    override fun unregisterLocationListener(listener: LocationManager.LocationListener) {
        locationListeners.remove(listener)
    }

    private fun notifyCurrentLocation(location: com.mercandalli.tracker.location.Location) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyCurrentLocation(location) })
            return
        }
        for (listener in locationListeners) {
            listener.onLocationChanged(location)
        }
    }

    private fun createLocationListener(): LocationListener {
        return object : LocationListener {
            override fun onLocationChanged(location: android.location.Location) {
                onNewLocation(location)
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}

            override fun onProviderEnabled(s: String) {}

            override fun onProviderDisabled(s: String) {}
        }
    }

    private fun onNewLocation(location: android.location.Location) {
        val currentLocation = Location.create(location)
        locationRepository.putLocation(currentLocation)
        notifyCurrentLocation(currentLocation)
    }
}
