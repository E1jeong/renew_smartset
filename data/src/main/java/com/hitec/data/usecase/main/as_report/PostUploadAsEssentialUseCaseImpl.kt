package com.hitec.data.usecase.main.as_report

import com.hitec.data.db.retrofit.HitecService
import com.hitec.data.db.retrofit.param.UploadAsEssentialParam
import com.hitec.data.model.toDomainModel
import com.hitec.domain.model.UploadAsEssential
import com.hitec.domain.usecase.main.as_report.PostUploadAsEssentialUseCase
import javax.inject.Inject

class PostUploadAsEssentialUseCaseImpl @Inject constructor(
    private val hitecService: HitecService,
) : PostUploadAsEssentialUseCase {
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
        fieldActionMain: String,
        fieldActionSub: String,
        consumeHouseNo: String,
        deviceSn: String
    ): Result<UploadAsEssential> = kotlin.runCatching {
        val param = UploadAsEssentialParam(
            cdmaNo = cdmaNo,
            nwk = nwk,
            firmware = firmware,
            serverName = serverName,
            serverSite = serverSite,
            fieldActionMain = fieldActionMain,
            fieldActionSub = fieldActionSub,
            consumeHouseNo = consumeHouseNo,
            deviceSn = deviceSn,
        ).toBody()

        val response = hitecService.postUploadAsEssential(
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