package com.hitec.data.db.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hitec.data.model.entity.LocalSiteEntity

@Dao
interface LocalSiteDao {
    @Query("SELECT * FROM T_LocalSite ORDER BY rowNumber DESC")
    fun getAll(): List<LocalSiteEntity>

    @Query("SELECT * FROM T_LocalSite ORDER BY rowNumber DESC LIMIT 1")
    fun getLatest(): LocalSiteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(localSite: List<LocalSiteEntity>)

    @Delete
    fun delete(localSite: List<LocalSiteEntity>)

    @Update
    fun update(localSite: List<LocalSiteEntity>)

    @Query("SELECT * FROM T_LocalSite WHERE siteNameKor LIKE :siteName OR siteNameEng LIKE :siteName")
    fun findSiteName(siteName: String): List<LocalSiteEntity>
}