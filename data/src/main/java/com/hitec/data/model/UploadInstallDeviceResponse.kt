package com.hitec.data.model

import com.hitec.domain.model.UploadInstallDevice
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "UploadInstallReponse") // 오타 주의
data class UploadInstallDeviceResponse(
    @PropertyElement(name = "userId") val userId: String,
    @PropertyElement(name = "userLevel") val userLevel: String,
    @PropertyElement(name = "localSite") val localSite: String,
    @PropertyElement(name = "returnCode") val returnCode: String?,
    @PropertyElement(name = "msgCode") val msgCode: String,
    @Element(name = "UploadResutInstallInfo") val uploadResultInstallInfo: UploadResultInstallInfo
)

@Xml(name = "UploadResutInstallInfo")
data class UploadResultInstallInfo(
    @PropertyElement(name = "rowNumber") val rowNumber: Int,
    @PropertyElement(name = "meterDeviceId") val meterDeviceId: String,
    @PropertyElement(name = "consumeHouseNo") val consumeHouseNo: String,
    @PropertyElement(name = "serverDeviceId") val serverDeviceId: String,
    @PropertyElement(name = "resultCd") val resultCd: String,
    @PropertyElement(name = "errorCd") val errorCd: String,
)

fun UploadInstallDeviceResponse.toDomainModel(): UploadInstallDevice = UploadInstallDevice(
    userId = this.userId,
    userLevel = this.userLevel,
    localSite = this.localSite,
    returnCode = this.returnCode ?: "",
    msgCode = this.msgCode,
    meterDeviceId = this.uploadResultInstallInfo.meterDeviceId,
    consumeHouseNo = this.uploadResultInstallInfo.consumeHouseNo,
    serverDeviceId = this.uploadResultInstallInfo.serverDeviceId,
    resultCd = this.uploadResultInstallInfo.resultCd,
    errorCd = this.uploadResultInstallInfo.errorCd
)