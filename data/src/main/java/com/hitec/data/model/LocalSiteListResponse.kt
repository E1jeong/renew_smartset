package com.hitec.data.model

import com.hitec.data.model.entity.LocalSiteEntity
import com.hitec.domain.model.LocalSite
import com.hitec.domain.model.LocalSiteModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "LocalSiteListReponse") //오타 주의
data class LocalSiteListResponse(
    @PropertyElement(name = "userId") val userId: String,
    @PropertyElement(name = "userLevel") val userLevel: Int,
    @PropertyElement(name = "localSite") val localSite: String,
    @PropertyElement(name = "returnCode") val returnCode: Int,
    @PropertyElement(name = "msgCode") val msgCode: Int,
    @Element(name = "LocalSiteInfo") val localSiteInfo: List<LocalSiteInfo>
)

@Xml(name = "LocalSiteInfo")
data class LocalSiteInfo(
    @PropertyElement(name = "rowNumber") val rowNumber: Int,
    @PropertyElement(name = "SiteId") val siteId: String,
    @PropertyElement(name = "SiteName") val siteName: String,
    @PropertyElement(name = "ConnectionType") val connectionType: Int,
    @PropertyElement(name = "VpnSecuwayVersion") val vpnSecuwayVersion: Int,
)

fun LocalSiteListResponse.toDomainModel(): LocalSite = LocalSite(
    userId = userId,
    userLevel = userLevel,
    localSite = localSite,
    returnCode = returnCode,
    msgCode = msgCode,
    localSiteInfo = localSiteInfo.map { response ->
        LocalSiteModel(
            rowNumber = response.rowNumber,
            siteId = response.siteId,
            siteName = response.siteName,
            connectionType = response.connectionType,
            vpnSecuwayVersion = response.vpnSecuwayVersion
        )
    }
)

fun LocalSiteListResponse.toEntity(): List<LocalSiteEntity> =
    localSiteInfo.map { info ->
        LocalSiteEntity(
            rowNumber = info.rowNumber,
            siteNameEng = info.siteId,
            siteNameKor = info.siteName,
            connectionType = info.connectionType,
            vpnSecuwayVersion = info.vpnSecuwayVersion
        )
    }