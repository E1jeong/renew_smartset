package com.hitec.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hitec.data.db.datastore.LoginScreenInfoPreference
import com.hitec.data.usecase.LoginScreenInfoUseCaseImpl
import com.hitec.domain.usecase.LoginScreenInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hitec_pref")

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    fun provideDatastore(context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideLoginScreenInfoUseCase(dataStore: DataStore<Preferences>): LoginScreenInfoUseCase {
        return LoginScreenInfoUseCaseImpl(LoginScreenInfoPreference(dataStore))
    }
}