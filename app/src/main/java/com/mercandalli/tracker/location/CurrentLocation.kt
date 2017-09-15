package com.mercandalli.tracker.location

import android.location.Location

import com.google.gson.annotations.SerializedName

class CurrentLocation private constructor(
        @SerializedName("latitude")
        val latitude: Double,

        @SerializedName("longitude")
        val longitude: Double,

        @SerializedName("speed")
        private val speed: Double,

        @SerializedName("altitude")
        val altitude: Double,

        @SerializedName("timestamp")
        val timestamp: Long) {

    override fun toString(): String {
        return "CurrentLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", speed=" + speed +
                ", altitude=" + altitude +
                ", timestamp=" + timestamp +
                '}'
    }

    companion object {

        internal fun create(location: Location): CurrentLocation {
            return CurrentLocation(
                    location.latitude,
                    location.longitude,
                    location.speed.toDouble(),
                    location.altitude,
                    location.time)
        }
    }
}
