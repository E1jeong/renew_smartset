package com.hitec.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hitec.data.model.entity.AsCodeEntity

@Dao
interface AsCodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<AsCodeEntity>)

    @Query("DELETE FROM T_AsCode")
    suspend fun delete()

    @Query("SELECT * FROM T_AsCode WHERE CD_GROUP_ID = :groupId AND CD_ID_SUB = 3")
    suspend fun getAsCode(groupId: String): List<AsCodeEntity>
}