package com.mercandalli.tracker.location

import android.util.Log
import com.mercandalli.tracker.main_thread.MainThreadPost
import java.util.ArrayList
import kotlin.collections.HashMap

@Suppress("unused")
internal class LocationRepositoryRam(
        private val mainThreadPost: MainThreadPost) : LocationRepository {

    private val locations = HashMap<Long, Location>()
    private var biggerLocationTimestamp: Long = -1L
    private val locationListeners = ArrayList<LocationRepository.LocationRepositoryListener>()

    override fun putLocation(location: Location) {
        Log.d("jm/debug", "putLocation: " + location)
        locations.put(location.timestamp, location)
        if (location.timestamp > biggerLocationTimestamp) {
            biggerLocationTimestamp = location.timestamp
        }
        notifyChanged()
    }

    override fun getLocation(): Location? {
        if (biggerLocationTimestamp == -1L || locations.isEmpty()) {
            return null
        }
        return locations[biggerLocationTimestamp]
    }

    override fun getLocations(): List<Location> {
        return ArrayList(locations.values)
    }

    override fun registerLocationRepositoryListener(listener: LocationRepository.LocationRepositoryListener) {
        if (locationListeners.contains(listener)) {
            return
        }
        locationListeners.add(listener)
    }

    override fun unregisterLocationRepositoryListener(listener: LocationRepository.LocationRepositoryListener) {
        locationListeners.remove(listener)
    }

    private fun notifyChanged() {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyChanged() })
            return
        }
        for (listener in locationListeners) {
            listener.onLocationChanged()
        }
    }
}