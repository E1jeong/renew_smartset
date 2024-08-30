package com.hitec.data.usecase.main.device_detail

import com.hitec.data.db.retrofit.HitecService
import com.hitec.data.db.retrofit.param.DownloadDeviceImageParam
import com.hitec.data.model.toDomainModel
import com.hitec.domain.model.DeviceImage
import com.hitec.domain.usecase.main.device_detail.PostDownloadDeviceImageUseCase
import javax.inject.Inject

class PostDownloadDeviceImageUseCaseImpl @Inject constructor(
    private val hitecService: HitecService,
) : PostDownloadDeviceImageUseCase {
    override suspend fun invoke(
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String,
        meterDeviceId: String,
        deviceTypeCd: String,
        meterCd: String?,
        photoTypeCd: Int
    ): Result<DeviceImage> = kotlin.runCatching {
        val param = DownloadDeviceImageParam(
            meterDeviceId = meterDeviceId,
            deviceTypeCd = deviceTypeCd,
            meterCd = meterCd,
            photoTypeCd = photoTypeCd,
        ).toBody()

        val response = hitecService.postDownloadDeviceImage(
            userId = userId,
            password = password,
            mobileId = mobileId,
            bluetoothId = bluetoothId,
            localSite = localSite,
            data = param
        )

        response.toDomainModel()
    }
}