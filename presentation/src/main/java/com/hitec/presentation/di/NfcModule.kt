package com.hitec.presentation.di

import com.hitec.presentation.nfc_lib.NfcManager
import com.hitec.presentation.nfc_lib.NfcRequest
import com.hitec.presentation.nfc_lib.NfcResponse
import dagger.Lazy
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
    fun provideNfcResponse(nfcManager: NfcManager): NfcResponse {
        return NfcResponse(nfcManager)
    }

    @Provides
    @Singleton
    fun provideNfcRequest(nfcManager: NfcManager): NfcRequest {
        return NfcRequest(nfcManager)
    }

    @Provides
    @Singleton
    fun provideNfcManager(nfcResponse: Lazy<NfcResponse>): NfcManager {
        return NfcManager(nfcResponse)
    }
}