package com.hitec.data.di

import com.hitec.data.usecase.login.FindLocalSiteNameUseCaseImpl
import com.hitec.data.usecase.login.GetLocalSiteUseCaseImpl
import com.hitec.data.usecase.main.DeleteInstallDbUseCaseImpl
import com.hitec.data.usecase.main.GetAsDeviceUseCaseImpl
import com.hitec.data.usecase.main.GetInstallDbUrlUseCaseImpl
import com.hitec.data.usecase.main.GetInstallDbUseCaseImpl
import com.hitec.data.usecase.main.GetInstallDeviceUseCaseImpl
import com.hitec.data.usecase.main.GetServerInfoUseCaseImpl
import com.hitec.data.usecase.main.GetSubAreaUseCaseImpl
import com.hitec.data.usecase.main.as_report.GetAsCodeUseCaseImpl
import com.hitec.data.usecase.main.as_report.PostUploadAsDeviceUseCaseImpl
import com.hitec.data.usecase.main.as_report.PostUploadAsEssentialUseCaseImpl
import com.hitec.data.usecase.main.as_report.UpdateAsDeviceUseCaseImpl
import com.hitec.data.usecase.main.device_detail.PostDownloadDeviceImageUseCaseImpl
import com.hitec.data.usecase.main.device_detail.PostDownloadableImageListUseCaseImpl
import com.hitec.data.usecase.main.device_detail.PostUploadInstallDeviceUseCaseImpl
import com.hitec.data.usecase.main.device_detail.PostUploadInstallEssentialUseCaseImpl
import com.hitec.data.usecase.main.device_detail.UpdateInstallDeviceUseCaseImpl
import com.hitec.data.usecase.main.search.GetAsDeviceListFromImeiAndSubAreaUseCaseImpl
import com.hitec.data.usecase.main.search.GetAsDeviceListFromSubAreaUseCaseImpl
import com.hitec.data.usecase.main.search.GetInstallDeviceListFromImeiAndSubAreaUseCaseImpl
import com.hitec.data.usecase.main.search.GetInstallDeviceListFromSubAreaUseCaseImpl
import com.hitec.domain.usecase.login.FindLocalSiteNameUseCase
import com.hitec.domain.usecase.login.GetLocalSiteUseCase
import com.hitec.domain.usecase.main.DeleteInstallDbUseCase
import com.hitec.domain.usecase.main.GetAsDeviceUseCase
import com.hitec.domain.usecase.main.GetInstallDbUrlUseCase
import com.hitec.domain.usecase.main.GetInstallDbUseCase
import com.hitec.domain.usecase.main.GetInstallDeviceUseCase
import com.hitec.domain.usecase.main.GetServerInfoUseCase
import com.hitec.domain.usecase.main.GetSubAreaUseCase
import com.hitec.domain.usecase.main.as_report.GetAsCodeUseCase
import com.hitec.domain.usecase.main.as_report.PostUploadAsDeviceUseCase
import com.hitec.domain.usecase.main.as_report.PostUploadAsEssentialUseCase
import com.hitec.domain.usecase.main.as_report.UpdateAsDeviceUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadDeviceImageUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadableImageListUseCase
import com.hitec.domain.usecase.main.device_detail.PostUploadInstallDeviceUseCase
import com.hitec.domain.usecase.main.device_detail.PostUploadInstallEssentialUseCase
import com.hitec.domain.usecase.main.device_detail.UpdateInstallDeviceUseCase
import com.hitec.domain.usecase.main.search.GetAsDeviceListFromImeiAndSubAreaUseCase
import com.hitec.domain.usecase.main.search.GetAsDeviceListFromSubAreaUseCase
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

    @Binds
    abstract fun bindPostUploadAsEssentialUseCase(uc: PostUploadAsEssentialUseCaseImpl): PostUploadAsEssentialUseCase

    @Binds
    abstract fun bindPostUploadAsDeviceUseCase(uc: PostUploadAsDeviceUseCaseImpl): PostUploadAsDeviceUseCase

    @Binds
    abstract fun bindPostUploadInstallEssentialUseCase(uc: PostUploadInstallEssentialUseCaseImpl): PostUploadInstallEssentialUseCase

    @Binds
    abstract fun bindPostUploadInstallDeviceUseCase(uc: PostUploadInstallDeviceUseCaseImpl): PostUploadInstallDeviceUseCase

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

    @Binds
    abstract fun bindGetAsDeviceUseCase(uc: GetAsDeviceUseCaseImpl): GetAsDeviceUseCase

    @Binds
    abstract fun bindGetAsDeviceListFromSubAreaUseCase(uc: GetAsDeviceListFromSubAreaUseCaseImpl): GetAsDeviceListFromSubAreaUseCase

    @Binds
    abstract fun bindGetAsDeviceListFromImeiAndSubAreaUseCase(uc: GetAsDeviceListFromImeiAndSubAreaUseCaseImpl): GetAsDeviceListFromImeiAndSubAreaUseCase

    @Binds
    abstract fun bindGetAsCodeUseCase(uc: GetAsCodeUseCaseImpl): GetAsCodeUseCase

    @Binds
    abstract fun bindGetServerInfoUseCase(uc: GetServerInfoUseCaseImpl): GetServerInfoUseCase

    @Binds
    abstract fun bindUpdateAsDeviceUseCase(uc: UpdateAsDeviceUseCaseImpl): UpdateAsDeviceUseCase
}