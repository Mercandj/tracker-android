package com.mercandalli.tracker.location

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(location: LocationEntity)

    @Query("SELECT * FROM location")
    fun load(): List<LocationEntity>
}
