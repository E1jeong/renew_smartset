package com.hitec.data.model

import com.hitec.domain.model.DownloadableImageList
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "DownloadImageListResponse")
data class DownloadImageListResponse(
    @PropertyElement(name = "userId") val userId: String,
    @PropertyElement(name = "userLevel") val userLevel: Int,
    @PropertyElement(name = "localSite") val localSite: String,
    @PropertyElement(name = "returnCode") val returnCode: String?,
    @PropertyElement(name = "msgCode") val msgCode: Int,
    @Element(name = "DownloadResultImageList") val downloadResultImageList: List<DownloadResultImageList>?
)

@Xml(name = "DownloadResultImageList")
data class DownloadResultImageList(
    @PropertyElement(name = "rowNumber") val rowNumber: Int,
    @PropertyElement(name = "meterDeviceId") val meterDeviceId: String,
    @PropertyElement(name = "deviceTypeCd") val deviceTypeCd: Int,
    @PropertyElement(name = "meterCd") val meterCd: Int?,
    @PropertyElement(name = "photoTypeCd") val photoTypeCd: Int,
    @PropertyElement(name = "resultCd") val resultCd: Int,
)

fun DownloadImageListResponse.toDomainModel(): List<DownloadableImageList>? =
    downloadResultImageList?.map {
        DownloadableImageList(
            rowNumber = it.rowNumber,
            meterDeviceId = it.meterDeviceId,
            deviceTypeCd = it.deviceTypeCd,
            meterCd = it.meterCd,
            photoTypeCd = it.photoTypeCd,
            resultCd = it.resultCd
        )
    }