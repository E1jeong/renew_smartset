package com.hitec.data.model

import com.hitec.domain.model.UploadAsDevice
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "UploadAsReponse") // 오타 주의
data class UploadAsDeviceResponse(
    @PropertyElement(name = "userId") val userId: String,
    @PropertyElement(name = "userLevel") val userLevel: String,
    @PropertyElement(name = "localSite") val localSite: String,
    @PropertyElement(name = "returnCode") val returnCode: String?,
    @PropertyElement(name = "msgCode") val msgCode: String,
    @Element(name = "UploadResultAsState") val uploadResultAsState: UploadResultAsState
)

@Xml(name = "UploadResultAsState")
data class UploadResultAsState(
    @PropertyElement(name = "rowNumber") val rowNumber: Int,
    @PropertyElement(name = "reportNo") val reportNo: String,
    @PropertyElement(name = "consumeHouseNo") val consumeHouseNo: String,
    @PropertyElement(name = "serverReportNo") val serverReportNo: String,
    @PropertyElement(name = "statusSet") val statusSet: String,
    @PropertyElement(name = "perNext") val perNext: String,
    @PropertyElement(name = "resultCd") val resultCd: String,
    @PropertyElement(name = "errorCd") val errorCd: String,
)

fun UploadAsDeviceResponse.toDomainModel(): UploadAsDevice = UploadAsDevice(
    userId = this.userId,
    userLevel = this.userLevel,
    localSite = this.localSite,
    returnCode = this.returnCode ?: "",
    msgCode = this.msgCode,
    reportNo = this.uploadResultAsState.reportNo,
    consumeHouseNo = this.uploadResultAsState.consumeHouseNo,
    serverReportNo = this.uploadResultAsState.serverReportNo,
    statusSet = this.uploadResultAsState.statusSet,
    perNext = this.uploadResultAsState.perNext,
    resultCd = this.uploadResultAsState.resultCd,
    errorCd = this.uploadResultAsState.errorCd
)
