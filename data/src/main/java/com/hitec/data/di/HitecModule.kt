package com.hitec.data.di

import com.hitec.data.usecase.GetLocalSiteUseCaseImpl
import com.hitec.domain.usecase.GetLocalSiteUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HitecModule {

    @Binds
    abstract fun bindGetLocalSiteUseCase(uc: GetLocalSiteUseCaseImpl): GetLocalSiteUseCase
}