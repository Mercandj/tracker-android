package com.mercandalli.tracker.location

interface LocationRepository {

    fun putLocation(location: Location)

    fun getLocation(): Location?

    fun getLocations(): List<Location>

    fun registerLocationRepositoryListener(listener: LocationRepositoryListener)

    fun unregisterLocationRepositoryListener(listener: LocationRepositoryListener)

    interface LocationRepositoryListener {

        fun onLocationChanged()
    }
}
