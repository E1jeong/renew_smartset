package com.hitec.data.di

import com.hitec.data.usecase.FindLocalSiteNameUseCaseImpl
import com.hitec.data.usecase.GetLocalSiteUseCaseImpl
import com.hitec.data.usecase.GetSubAreaUseCaseImpl
import com.hitec.domain.usecase.FindLocalSiteNameUseCase
import com.hitec.domain.usecase.GetLocalSiteUseCase
import com.hitec.domain.usecase.GetSubAreaUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HitecModule {

    @Binds
    abstract fun bindGetLocalSiteUseCase(uc: GetLocalSiteUseCaseImpl): GetLocalSiteUseCase

    @Binds
    abstract fun bindFindLocalSiteNameUseCase(uc: FindLocalSiteNameUseCaseImpl): FindLocalSiteNameUseCase

    @Binds
    abstract fun bindGetSubAreaUseCase(uc: GetSubAreaUseCaseImpl): GetSubAreaUseCase
}