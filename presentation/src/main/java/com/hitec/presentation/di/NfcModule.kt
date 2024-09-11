package com.hitec.presentation.di

import com.hitec.presentation.nfc_lib.NfcManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NfcModule {

    @Provides
    @Singleton
    fun provideNfcManager(): NfcManager {
        return NfcManager()
    }
}