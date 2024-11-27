package com.hitec.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hitec.data.model.entity.AsDeviceEntity

@Dao
interface AsDeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<AsDeviceEntity>)

    @Query("SELECT * FROM T_AsDevice")
    suspend fun getAll(): List<AsDeviceEntity>

    @Query("DELETE FROM T_AsDevice")
    suspend fun delete()

    @Query("DELETE FROM T_AsDevice WHERE meterDeviceId = :meterDeviceId")
    suspend fun deleteByMeterDeviceId(meterDeviceId: String)

    @Query("SELECT * FROM T_AsDevice WHERE meterDeviceId = :meterDeviceId LIMIT 1")
    suspend fun findByMeterDeviceId(meterDeviceId: String): AsDeviceEntity?

    @Update
    suspend fun update(entity: AsDeviceEntity)

    @Query("SELECT * FROM T_AsDevice WHERE areaBig = :subArea")
    suspend fun getAsDeviceListWithSubArea(subArea: String): List<AsDeviceEntity>

    @Query(
        "SELECT * FROM T_AsDevice " +
                "WHERE (:subArea != '' AND areaBig = :subArea AND (REPLACE(nwk, '-', '') LIKE '%' || REPLACE(:imei, '-', '') || '%')) " +
                "OR (:subArea = '' AND REPLACE(nwk, '-', '') LIKE '%' || REPLACE(:imei, '-', '') || '%')"
    )
    suspend fun getAsDeviceListWithImeiAndSubArea(
        subArea: String,
        imei: String
    ): List<AsDeviceEntity>
}