package com.hitec.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hitec.data.model.entity.InstallDeviceEntity

@Dao
interface InstallDeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<InstallDeviceEntity>): List<Long>

    @Query("SELECT * FROM T_InstallDevice")
    suspend fun getAll(): List<InstallDeviceEntity>

    @Query("DELETE FROM T_InstallDevice")
    suspend fun delete()

    @Query("SELECT * FROM T_InstallDevice WHERE AreaBig = :subArea")
    suspend fun getDeviceListWithSubArea(subArea: String): List<InstallDeviceEntity>

    @Query(
        "SELECT * FROM T_InstallDevice " +
                "WHERE (:subArea != '' AND AreaBig = :subArea AND (REPLACE(nwk, '-', '') LIKE '%' || REPLACE(:imei, '-', '') || '%')) " +
                "OR (:subArea = '' AND REPLACE(nwk, '-', '') LIKE '%' || REPLACE(:imei, '-', '') || '%')"
    )
    suspend fun getDeviceListWithImeiAndSubArea(
        subArea: String,
        imei: String
    ): List<InstallDeviceEntity>
}