package com.hitec.data.di

import android.content.Context
import androidx.room.Room
import com.hitec.data.db.room.ApplicationDatabase
import com.hitec.data.db.room.dao.AsCodeDao
import com.hitec.data.db.room.dao.AsDeviceDao
import com.hitec.data.db.room.dao.InstallDeviceDao
import com.hitec.data.db.room.dao.LocalSiteDao
import com.hitec.data.db.room.dao.ServerInfoDao
import com.hitec.data.db.room.dao.SubAreaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(context: Context): ApplicationDatabase {
        return Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            ApplicationDatabase.DB_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideLocalSiteDao(database: ApplicationDatabase): LocalSiteDao {
        return database.localSiteDao()
    }

    @Provides
    @Singleton
    fun provideSubAreaDao(database: ApplicationDatabase): SubAreaDao {
        return database.subAreaDao()
    }

    @Provides
    @Singleton
    fun provideInstallDeviceDao(database: ApplicationDatabase): InstallDeviceDao {
        return database.installDeviceDao()
    }

    @Provides
    @Singleton
    fun provideServerInfoDao(database: ApplicationDatabase): ServerInfoDao {
        return database.serverInfoDao()
    }

    @Provides
    @Singleton
    fun provideAsDeviceDao(database: ApplicationDatabase): AsDeviceDao {
        return database.asDeviceDao()
    }

    @Provides
    @Singleton
    fun provideAsCodeDao(database: ApplicationDatabase): AsCodeDao {
        return database.asCodeDao()
    }
}