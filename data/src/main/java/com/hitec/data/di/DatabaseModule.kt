package com.hitec.data.di

import android.content.Context
import androidx.room.Room
import com.hitec.data.db.room.ApplicationDatabase
import com.hitec.data.db.room.dao.LocalSiteDao
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
}