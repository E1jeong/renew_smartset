package com.hitec.data.usecase.main.device_detail

import com.hitec.data.db.retrofit.HitecService
import com.hitec.data.db.retrofit.param.UploadInstallEssentialParam
import com.hitec.data.model.toDomainModel
import com.hitec.domain.model.UploadInstallEssential
import com.hitec.domain.usecase.main.device_detail.PostUploadInstallEssentialUseCase
import javax.inject.Inject

class PostUploadInstallEssentialUseCaseImpl @Inject constructor(
    private val hitecService: HitecService
) : PostUploadInstallEssentialUseCase {
    override suspend fun invoke(
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String,
        cdmaNo: String,
        nwk: String,
        firmware: String,
        serverName: String,
        serverSite: String,
        consumeHouseNo: String,
        deviceSn: String
    ): Result<UploadInstallEssential> = kotlin.runCatching {
        val param = UploadInstallEssentialParam(
            cdmaNo = cdmaNo,
            nwk = nwk,
            firmware = firmware,
            serverName = serverName,
            serverSite = serverSite,
            consumeHouseNo = consumeHouseNo,
            deviceSn = deviceSn,
        ).toBody()

        val response = hitecService.postUploadInstallEssential(
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