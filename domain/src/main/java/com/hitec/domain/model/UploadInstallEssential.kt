package com.hitec.domain.model

data class UploadInstallEssential(
    val userId: String,
    val userLevel: Int,
    val localSite: String,
    val returnCode: Int,
    val msgCode: Int,
)
