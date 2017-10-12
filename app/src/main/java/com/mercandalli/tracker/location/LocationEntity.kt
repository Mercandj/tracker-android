package com.mercandalli.tracker.location

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "location")
class LocationEntity constructor(
        @PrimaryKey
        val id: String,

        @ColumnInfo(name = "user_id")
        val userId: Int,

        @ColumnInfo(name = "latitude")
        val latitude: Double,

        @ColumnInfo(name = "longitude")
        val longitude: Double,

        @ColumnInfo(name = "speed")
        val speed: Float,

        @ColumnInfo(name = "altitude")
        val altitude: Double,

        @ColumnInfo(name = "timestamp")
        val timestamp: Long) {


    companion object {
        fun create(location: Location): LocationEntity {
            return LocationEntity(
                    0.toString() + "-" + location.timestamp,
                    0,
                    location.latitude,
                    location.longitude,
                    location.speed,
                    location.altitude,
                    location.timestamp)
        }
    }

    fun toLocation(): Location {
        return Location(
                latitude,
                longitude,
                speed,
                altitude,
                timestamp)
    }
}
