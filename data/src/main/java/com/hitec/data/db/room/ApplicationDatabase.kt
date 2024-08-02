package com.hitec.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hitec.data.db.room.dao.InstallDeviceDao
import com.hitec.data.db.room.dao.LocalSiteDao
import com.hitec.data.db.room.dao.ServerInfoDao
import com.hitec.data.db.room.dao.SubAreaDao
import com.hitec.data.model.entity.InstallDeviceEntity
import com.hitec.data.model.entity.LocalSiteEntity
import com.hitec.data.model.entity.ServerInfoEntity
import com.hitec.data.model.entity.SubAreaEntity

@Database(
    entities = [
        LocalSiteEntity::class,
        SubAreaEntity::class,
        InstallDeviceEntity::class,
        ServerInfoEntity::class,
    ],
    version = 4,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "roomDatabase.db"
    }

    abstract fun localSiteDao(): LocalSiteDao
    abstract fun subAreaDao(): SubAreaDao
    abstract fun installDeviceDao(): InstallDeviceDao
    abstract fun serverInfoDao(): ServerInfoDao
}