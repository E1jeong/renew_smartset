package com.hitec.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hitec.data.model.entity.ServerInfoEntity

@Dao
interface ServerInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<ServerInfoEntity>)

    @Query("DELETE FROM T_ServerInfo")
    suspend fun delete()

    @Query("SELECT * FROM T_ServerInfo")
    suspend fun getAll(): List<ServerInfoEntity>
}