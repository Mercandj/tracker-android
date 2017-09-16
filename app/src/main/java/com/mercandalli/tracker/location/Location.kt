package com.mercandalli.tracker.location

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

class Location private constructor(
        @SerializedName("latitude")
        val latitude: Double,

        @SerializedName("longitude")
        val longitude: Double,

        @SerializedName("speed")
        val speed: Float,

        @SerializedName("altitude")
        val altitude: Double,

        @SerializedName("timestamp")
        val timestamp: Long) {

    override fun toString(): String {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", speed=" + speed +
                ", altitude=" + altitude +
                ", timestamp=" + timestamp +
                '}'
    }

    fun toLatLng(): LatLng {
        return LatLng(latitude, longitude)
    }

    companion object {

        internal fun create(location: android.location.Location): Location {
            return Location(
                    location.latitude,
                    location.longitude,
                    location.speed,
                    location.altitude,
                    location.time)
        }
    }
}
