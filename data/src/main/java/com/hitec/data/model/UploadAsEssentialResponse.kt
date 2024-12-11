package com.hitec.data.model

import com.hitec.domain.model.UploadAsEssential
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "nbAsUpload")
data class UploadAsEssentialResponse(
    @PropertyElement(name = "userId") val userId: String,
    @PropertyElement(name = "userLevel") val userLevel: Int,
    @PropertyElement(name = "localSite") val localSite: String,
    @PropertyElement(name = "returnCode") val returnCode: Int,
    @PropertyElement(name = "msgCode") val msgCode: Int,
)

fun UploadAsEssentialResponse.toDomainModel(): UploadAsEssential = UploadAsEssential(
    userId = userId,
    userLevel = userLevel,
    localSite = localSite,
    returnCode = returnCode,
    msgCode = msgCode
)