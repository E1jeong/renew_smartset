package com.hitec.data.model

import com.hitec.domain.model.DeviceImage
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "DownloadImageDateReponse")
data class DownloadImageDataResponse(
    @PropertyElement(name = "userId") val userId: String,
    @PropertyElement(name = "userLevel") val userLevel: Int,
    @PropertyElement(name = "localSite") val localSite: String,
    @PropertyElement(name = "returnCode") val returnCode: String,
    @PropertyElement(name = "msgCode") val msgCode: Int,
    @Element(name = "DownloadResultImageData") val downloadResultImageData: DownloadResultImageData
)

@Xml(name = "DownloadResultImageData")
data class DownloadResultImageData(
    @PropertyElement(name = "rowNumber") val rowNumber: Int,
    @PropertyElement(name = "meterDeviceId") val meterDeviceId: String,
    @PropertyElement(name = "meterCd") val meterCd: String?,
    @PropertyElement(name = "photoTypeCd") val photoTypeCd: Int,
    @PropertyElement(name = "imageFileName") val imageFileName: String,
    @PropertyElement(name = "imageFileData") val imageFileData: String,
    @PropertyElement(name = "resultCd") val resultCd: String,
)

fun DownloadImageDataResponse.toDomainModel(): DeviceImage = DeviceImage(
    imageName = downloadResultImageData.imageFileName,
    imageData = downloadResultImageData.imageFileData
)