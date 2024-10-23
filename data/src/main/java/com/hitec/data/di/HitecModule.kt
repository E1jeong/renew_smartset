package com.hitec.data.di

import com.hitec.data.usecase.login.FindLocalSiteNameUseCaseImpl
import com.hitec.data.usecase.login.GetLocalSiteUseCaseImpl
import com.hitec.data.usecase.main.DeleteInstallDbUseCaseImpl
import com.hitec.data.usecase.main.GetInstallDbUrlUseCaseImpl
import com.hitec.data.usecase.main.GetInstallDbUseCaseImpl
import com.hitec.data.usecase.main.GetInstallDeviceUseCaseImpl
import com.hitec.data.usecase.main.GetSubAreaUseCaseImpl
import com.hitec.data.usecase.main.device_detail.PostDownloadDeviceImageUseCaseImpl
import com.hitec.data.usecase.main.device_detail.PostDownloadableImageListUseCaseImpl
import com.hitec.data.usecase.main.device_detail.UpdateInstallDeviceUseCaseImpl
import com.hitec.data.usecase.main.search.GetInstallDeviceListFromImeiAndSubAreaUseCaseImpl
import com.hitec.data.usecase.main.search.GetInstallDeviceListFromSubAreaUseCaseImpl
import com.hitec.domain.usecase.login.FindLocalSiteNameUseCase
import com.hitec.domain.usecase.login.GetLocalSiteUseCase
import com.hitec.domain.usecase.main.DeleteInstallDbUseCase
import com.hitec.domain.usecase.main.GetInstallDbUrlUseCase
import com.hitec.domain.usecase.main.GetInstallDbUseCase
import com.hitec.domain.usecase.main.GetInstallDeviceUseCase
import com.hitec.domain.usecase.main.GetSubAreaUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadDeviceImageUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadableImageListUseCase
import com.hitec.domain.usecase.main.device_detail.UpdateInstallDeviceUseCase
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

    @Binds
    abstract fun bindPostDownloadableImageListUseCase(uc: PostDownloadableImageListUseCaseImpl): PostDownloadableImageListUseCase

    @Binds
    abstract fun bindPostDownloadDeviceImageUseCase(uc: PostDownloadDeviceImageUseCaseImpl): PostDownloadDeviceImageUseCase

    //======================================
    //Room module
    //======================================
    @Binds
    abstract fun bindGetInstallDeviceUseCase(uc: GetInstallDeviceUseCaseImpl): GetInstallDeviceUseCase

    @Binds
    abstract fun bindGetInstallDeviceListFromSubAreaUseCase(uc: GetInstallDeviceListFromSubAreaUseCaseImpl): GetInstallDeviceListFromSubAreaUseCase

    @Binds
    abstract fun bindGetInstallDeviceListFromImeiAndSubAreaUseCase(uc: GetInstallDeviceListFromImeiAndSubAreaUseCaseImpl): GetInstallDeviceListFromImeiAndSubAreaUseCase

    @Binds
    abstract fun bindUpdateInstallDeviceUseCase(uc: UpdateInstallDeviceUseCaseImpl): UpdateInstallDeviceUseCase
}