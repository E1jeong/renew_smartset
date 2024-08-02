package com.hitec.data.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "DownloadInstallDBReponse") //오타 주의
data class DownloadInstallDbResponse(
    @PropertyElement(name = "userId")
    val userId: String,
    @PropertyElement(name = "userLevel")
    val userLevel: String,
    @PropertyElement(name = "localSite")
    val localSite: String,
    @PropertyElement(name = "returnCode")
    val returnCode: Int,
    @PropertyElement(name = "msgCode")
    val msgCode: String,
    @Element(name = "InstallDBInfo")
    val installDBInfo: List<InstallDbInfo>,
)

@Xml(name = "InstallDBInfo")
data class InstallDbInfo(
    @PropertyElement(name = "rowNumber")
    val rowNumber: Int,
    @PropertyElement(name = "localSite")
    val localSite: String,
    @PropertyElement(name = "areaCd")
    val areaCd: String?,
    @PropertyElement(name = "siteUrl")
    val siteUrl: String,
)
