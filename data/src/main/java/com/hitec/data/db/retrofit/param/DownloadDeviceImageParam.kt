package com.hitec.data.db.retrofit.param

data class DownloadDeviceImageParam(
    val meterDeviceId: String,
    val deviceTypeCd: String,
    val meterCd: String? = "",
    val photoTypeCd: Int,
) {
    fun toBody(): String =
        "meterDeviceId^deviceTypeCd^meterCd^photoTypeCd|$meterDeviceId^$deviceTypeCd^$meterCd^$photoTypeCd"
}
