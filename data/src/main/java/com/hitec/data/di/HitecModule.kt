package com.hitec.data.di

import com.hitec.data.usecase.DeleteInstallDbUseCaseImpl
import com.hitec.data.usecase.FindLocalSiteNameUseCaseImpl
import com.hitec.data.usecase.GetInstallDbUrlUseCaseImpl
import com.hitec.data.usecase.GetInstallDbUseCaseImpl
import com.hitec.data.usecase.GetInstallDeviceListFromImeiAndSubAreaUseCaseImpl
import com.hitec.data.usecase.GetInstallDeviceListFromSubAreaUseCaseImpl
import com.hitec.data.usecase.GetInstallDeviceUseCaseImpl
import com.hitec.data.usecase.GetLocalSiteUseCaseImpl
import com.hitec.data.usecase.GetSubAreaUseCaseImpl
import com.hitec.domain.usecase.DeleteInstallDbUseCase
import com.hitec.domain.usecase.FindLocalSiteNameUseCase
import com.hitec.domain.usecase.GetInstallDbUrlUseCase
import com.hitec.domain.usecase.GetInstallDbUseCase
import com.hitec.domain.usecase.GetInstallDeviceUseCase
import com.hitec.domain.usecase.GetLocalSiteUseCase
import com.hitec.domain.usecase.GetSubAreaUseCase
import com.hitec.domain.usecase.main.search.GetInstallDeviceListFromImeiAndSubAreaUseCase
import com.hitec.domain.usecase.main.search.GetInstallDeviceListFromSubAreaUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HitecModule {

    //======================================
    //Retrofit module
    //======================================
    @Binds
    abstract fun bindGetLocalSiteUseCase(uc: GetLocalSiteUseCaseImpl): GetLocalSiteUseCase

    @Binds
    abstract fun bindFindLocalSiteNameUseCase(uc: FindLocalSiteNameUseCaseImpl): FindLocalSiteNameUseCase

    @Binds
    abstract fun bindGetSubAreaUseCase(uc: GetSubAreaUseCaseImpl): GetSubAreaUseCase

    @Binds
    abstract fun bindGetInstallDbUrlUseCase(uc: GetInstallDbUrlUseCaseImpl): GetInstallDbUrlUseCase

    @Binds
    abstract fun bindGetInstallDbUseCase(uc: GetInstallDbUseCaseImpl): GetInstallDbUseCase

    @Binds
    abstract fun bindDeleteInstallDbUseCase(uc: DeleteInstallDbUseCaseImpl): DeleteInstallDbUseCase

    //======================================
    //Room module
    //======================================
    @Binds
    abstract fun bindGetInstallDeviceUseCase(uc: GetInstallDeviceUseCaseImpl): GetInstallDeviceUseCase

    @Binds
    abstract fun bindGetInstallDeviceListFromSubAreaUseCase(uc: GetInstallDeviceListFromSubAreaUseCaseImpl): GetInstallDeviceListFromSubAreaUseCase

    @Binds
    abstract fun bindGetInstallDeviceListFromImeiAndSubAreaUseCase(uc: GetInstallDeviceListFromImeiAndSubAreaUseCaseImpl): GetInstallDeviceListFromImeiAndSubAreaUseCase
}