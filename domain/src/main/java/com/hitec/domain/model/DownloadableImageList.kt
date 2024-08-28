package com.hitec.domain.model

data class DownloadableImageList(
    val rowNumber: Int,
    val meterDeviceId: String,
    val deviceTypeCd: Int,
    val meterCd: Int?,
    val photoTypeCd: Int,
    val resultCd: Int,
)
