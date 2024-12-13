package com.hitec.data.model

import com.hitec.domain.model.UploadInstallEssential
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "UploadInstallReponse") // 오타주의
data class UploadInstallEssentialResponse(
    @PropertyElement(name = "userId") val userId: String,
    @PropertyElement(name = "userLevel") val userLevel: Int,
    @PropertyElement(name = "localSite") val localSite: String,
    @PropertyElement(name = "returnCode") val returnCode: Int,
    @PropertyElement(name = "msgCode") val msgCode: Int,
)

fun UploadInstallEssentialResponse.toDomainModel(): UploadInstallEssential = UploadInstallEssential(
    userId = userId,
    userLevel = userLevel,
    localSite = localSite,
    returnCode = returnCode,
    msgCode = msgCode
)