package com.mercandalli.tracker.location

import com.mercandalli.tracker.main_thread.MainThreadPost
import java.util.concurrent.Executor

internal class LocationRepositoryDatabase(
        private val mainThreadPost: MainThreadPost,
        private val locationDao: LocationDao,
        private val executor: Executor) : LocationRepository {

    private val locations = ArrayList<Location>()
    private val locationListeners = ArrayList<LocationRepository.LocationRepositoryListener>()

    init {
        executor.execute {
            synchronized(locations) {
                val locationsSync = getLocationsSync()
                locations.clear()
                locations.addAll(locationsSync)
                notifyChanged()
            }
        }
    }

    override fun putLocation(location: Location) {
        executor.execute {
            val locationEntity = LocationEntity.create(location)
            locationDao.save(locationEntity)
            notifyChanged()
        }
    }

    override fun getLocation(): Location? {
        // TODO - to improve in sql
        synchronized(locations) {
            var lastLocation: Location? = null
            for (location in locations) {
                if (lastLocation == null || location.timestamp > lastLocation.timestamp) {
                    lastLocation = location
                }
            }
            return lastLocation
        }
    }

    override fun getLocations(): List<Location> {
        return ArrayList(locations)
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

    private fun getLocationsSync(): List<Location> {
        val loaded = locationDao.load()
        val result = ArrayList<Location>()
        loaded.mapTo(result) { it.toLocation() }
        return result
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
