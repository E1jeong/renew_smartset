package com.hitec.domain.model

/*
AS report upload 진행시 해당 단말이 NB 제품인지 아닌지 판별해주는 통신에서 사용
즉 UploadAsEssential return에서 사용
 */
data class UploadAsEssential(
    val userId: String,
    val userLevel: Int,
    val localSite: String,
    val returnCode: Int,
    val msgCode: Int,
)
