package com.hitec.domain.usecase.main.as_report

import com.hitec.domain.model.UploadAsDevice

interface PostUploadAsDeviceUseCase {

    suspend operator fun invoke(
//        method: String, 해당 부분은 retrofit service에서 하드코딩 되어있음
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String,

        //down to body param
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
        firmware: String,
    ): Result<UploadAsDevice>
}