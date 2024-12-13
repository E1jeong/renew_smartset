package com.hitec.domain.model

data class UploadInstallDevice(
    val userId: String,
    val userLevel: String,
    val localSite: String,
    val returnCode: String?,
    val msgCode: String,
    val meterDeviceId: String,
    val consumeHouseNo: String,
    val serverDeviceId: String,
    val resultCd: String,
    val errorCd: String,
)
