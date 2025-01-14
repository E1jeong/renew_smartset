package com.hitec.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hitec.data.model.entity.InstallDeviceEntity
import com.hitec.domain.model.InstallDevice

@Dao
interface InstallDeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<InstallDeviceEntity>): List<Long>

    @Query("SELECT * FROM T_InstallDevice")
    suspend fun getAll(): List<InstallDeviceEntity>

    @Query("DELETE FROM T_InstallDevice")
    suspend fun delete()

    @Query("SELECT * FROM T_InstallDevice WHERE nwk = :imei")
    suspend fun getDeviceWithImei(imei: String): InstallDevice

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

    @Query("SELECT * FROM T_InstallDevice WHERE meterDeviceId = :meterDeviceId LIMIT 1")
    suspend fun findByMeterDeviceId(meterDeviceId: String): InstallDeviceEntity?

    @Update
    suspend fun updateInstallDevice(installDeviceEntity: InstallDeviceEntity)
}