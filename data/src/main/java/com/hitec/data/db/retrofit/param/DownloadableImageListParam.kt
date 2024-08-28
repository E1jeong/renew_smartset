package com.hitec.data.db.retrofit.param

data class DownloadableImageListParam(
    val meterDeviceId: String,
    val deviceTypeCd: String,
) {
    fun toBody(): String {
        return "meterDeviceId^deviceTypeCd|$meterDeviceId^$deviceTypeCd"
    }
}
