package com.hitec.data.usecase.main.as_report

import com.hitec.data.db.retrofit.HitecService
import com.hitec.data.db.retrofit.param.UploadAsDeviceParam
import com.hitec.data.model.toDomainModel
import com.hitec.domain.model.UploadAsDevice
import com.hitec.domain.usecase.main.as_report.PostUploadAsDeviceUseCase
import javax.inject.Inject

class PostUploadAsDeviceUseCaseImpl @Inject constructor(
    val hitecService: HitecService
) : PostUploadAsDeviceUseCase {
    override suspend fun invoke(
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String,
        modTypeCd: String,
        reportNo: String,
        siteId: String,
        receiptDt: String,
        receiptUserId: String,
        uploadResultCd: String,
        uploadErrorCd: String,
        receiptType: String,
        receiptMemo: String,
        consumeHouseNo: String,
        firstSetDt: String,
        meterMethodCd: String,
        deviceSn: String,
        pan: String,
        nwk: String,
        terminalTypeCd: String,
        meterTypeCd: String,
        deviceTypeCd: String,
        communicationTypeCd: String,
        telecomTypeCd: String,
        productYear: String,
        deviceModelCd: String,
        caliberCd: String,
        fieldActionMain: String,
        fieldActionSub: String,
        fieldActionMemo: String,
        analysisType: String,
        analysisTypeDetail: String,
        statusSet: String,
        perNext: String,
        installCompanyNm: String,
        firmware: String
    ): Result<UploadAsDevice> = kotlin.runCatching {

        val param = UploadAsDeviceParam(
            modTypeCd = modTypeCd,
            reportNo = reportNo,
            siteId = siteId,
            receiptDt = receiptDt,
            receiptUserId = receiptUserId,
            uploadResultCd = uploadResultCd,
            uploadErrorCd = uploadErrorCd,
            receiptType = receiptType,
            receiptMemo = receiptMemo,
            consumeHouseNo = consumeHouseNo,
            firstSetDt = firstSetDt,
            meterMethodCd = meterMethodCd,
            deviceSn = deviceSn,
            pan = pan,
            nwk = nwk,
            terminalTypeCd = terminalTypeCd,
            meterTypeCd = meterTypeCd,
            deviceTypeCd = deviceTypeCd,
            communicationTypeCd = communicationTypeCd,
            telecomTypeCd = telecomTypeCd,
            productYear = productYear,
            deviceModelCd = deviceModelCd,
            caliberCd = caliberCd,
            fieldActionMain = fieldActionMain,
            fieldActionSub = fieldActionSub,
            fieldActionMemo = fieldActionMemo,
            analysisType = analysisType,
            analysisTypeDetail = analysisTypeDetail,
            statusSet = statusSet,
            perNext = perNext,
            installCompanyNm = installCompanyNm,
            firmware = firmware,
        ).toBody()

        val response = hitecService.postUploadAsDevice(
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