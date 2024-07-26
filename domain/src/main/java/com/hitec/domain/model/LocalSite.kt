package com.hitec.domain.model

data class LocalSite(
    val userId: String,
    val userLevel: Int,
    val localSite: String,
    val returnCode: Int,
    val msgCode: Int,
    val localSiteInfo: List<LocalSiteModel>,
)

data class LocalSiteModel(
    val rowNumber: Int,
    val siteId: String,
    val siteName: String,
    val connectionType: Int,
    val vpnSecuwayVersion: Int,
)