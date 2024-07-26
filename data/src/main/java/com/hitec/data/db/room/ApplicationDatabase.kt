package com.hitec.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hitec.data.db.room.dao.LocalSiteDao
import com.hitec.data.model.entity.LocalSiteEntity

@Database(
    entities = [LocalSiteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "roomDatabase.db"
    }

    abstract fun localSiteDao(): LocalSiteDao
}