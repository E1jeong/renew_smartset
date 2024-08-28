package com.hitec.data.usecase.main.device_detail

import com.hitec.data.db.retrofit.HitecService
import com.hitec.data.db.retrofit.param.DownloadableImageListParam
import com.hitec.data.model.toDomainModel
import com.hitec.domain.model.DownloadableImageList
import com.hitec.domain.usecase.main.device_detail.PostDownloadableImageListUseCase
import javax.inject.Inject

class PostDownloadableImageListUseCaseImpl @Inject constructor(
    private val hitecService: HitecService,
) : PostDownloadableImageListUseCase {
    override suspend fun invoke(
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String,
        meterDeviceId: String,
        deviceTypeCd: String,
    ): Result<List<DownloadableImageList>> = kotlin.runCatching {
        val param = DownloadableImageListParam(
            meterDeviceId = meterDeviceId,
            deviceTypeCd = deviceTypeCd,
        ).toBody()

        val response = hitecService.postDownloadableImageList(
            userId = userId,
            password = password,
            mobileId = mobileId,
            bluetoothId = bluetoothId,
            localSite = localSite,
            data = param
        )
        response.toDomainModel() ?: emptyList()
    }
}