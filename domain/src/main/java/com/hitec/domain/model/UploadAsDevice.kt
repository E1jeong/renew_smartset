package com.hitec.domain.model

data class UploadAsDevice(
    val userId: String,
    val userLevel: String,
    val localSite: String,
    val returnCode: String?,
    val msgCode: String,
    val reportNo: String,
    val consumeHouseNo: String,
    val serverReportNo: String,
    val statusSet: String,
    val perNext: String,
    val resultCd: String,
    val errorCd: String,
)
