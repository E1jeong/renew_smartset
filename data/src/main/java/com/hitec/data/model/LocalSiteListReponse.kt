package com.hitec.data.model

import com.hitec.data.model.entity.LocalSiteEntity
import com.hitec.domain.model.LocalSite
import com.hitec.domain.model.LocalSiteData
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "LocalSiteListReponse")
data class LocalSiteListReponse(
    @PropertyElement(name = "userId") val userId: String,
    @PropertyElement(name = "userLevel") val userLevel: Int,
    @PropertyElement(name = "localSite") val localSite: String,
    @PropertyElement(name = "returnCode") val returnCode: Int,
    @PropertyElement(name = "msgCode") val msgCode: Int,
    @Element(name = "LocalSiteInfo") val LocalSiteInfo: List<LocalSiteInfo>
)

@Xml(name = "LocalSiteInfo")
data class LocalSiteInfo(
    @PropertyElement(name = "rowNumber") val rowNumber: Int,
    @PropertyElement(name = "SiteId") val SiteId: String,
    @PropertyElement(name = "SiteName") val SiteName: String,
    @PropertyElement(name = "ConnectionType") val ConnectionType: Int,
    @PropertyElement(name = "VpnSecuwayVersion") val VpnSecuwayVersion: Int,
)

fun LocalSiteListReponse.toDomainModel(): LocalSite = LocalSite(
    userId = userId,
    userLevel = userLevel,
    localSite = localSite,
    returnCode = returnCode,
    msgCode = msgCode,
    LocalSiteInfo = LocalSiteInfo.map { response ->
        LocalSiteData(
            rowNumber = response.rowNumber,
            SiteId = response.SiteId,
            SiteName = response.SiteName,
            ConnectionType = response.ConnectionType,
            VpnSecuwayVersion = response.VpnSecuwayVersion
        )
    }
)

fun LocalSiteListReponse.toEntity(): List<LocalSiteEntity> =
    LocalSiteInfo.map { info ->
        LocalSiteEntity(
            rowNumber = info.rowNumber,
            siteNameEng = info.SiteId, // 가정: SiteId가 영어 이름을 나타낸다고 가정
            siteNameKor = info.SiteName, // 가정: SiteName이 한국어 이름을 나타낸다고 가정
            connectionType = info.ConnectionType,
            vpnSecuwayVersion = info.VpnSecuwayVersion
        )
    }