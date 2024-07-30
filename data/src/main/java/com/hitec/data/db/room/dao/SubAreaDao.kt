package com.hitec.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hitec.data.model.entity.SubAreaEntity

@Dao
interface SubAreaDao {

    @Query("SELECT * FROM T_SubArea ORDER BY rowNumber DESC")
    fun getAll(): List<SubAreaEntity>

    @Query("SELECT * FROM T_SubArea ORDER BY rowNumber DESC LIMIT 1")
    fun getLatest(): SubAreaEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(subArea: List<SubAreaEntity>)

    @Query("DELETE FROM T_SubArea")
    fun delete()

    @Update
    fun update(subArea: List<SubAreaEntity>)
}
