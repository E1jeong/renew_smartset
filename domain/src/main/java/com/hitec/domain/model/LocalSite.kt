package com.hitec.domain.model

data class LocalSite(
    val userId: String,
    val userLevel: Int,
    val localSite: String,
    val returnCode: Int,
    val msgCode: Int,
    val LocalSiteInfo: List<LocalSiteData>,
)

data class LocalSiteData(
    val rowNumber: Int,
    val SiteId: String,
    val SiteName: String,
    val ConnectionType: Int,
    val VpnSecuwayVersion: Int,
)