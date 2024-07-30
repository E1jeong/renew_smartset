package com.hitec.domain.model

data class SubArea(
    val userId: String,
    val userLevel: Int,
    val localSite: String,
    val returnCode: Int,
    val msgCode: Int,
    val subAreaInfo: List<SubAreaModel>,
)

data class SubAreaModel(
    val rowNumber: Int,
    val areaCd: Int,
    val parentAreaCd: Int,
    val areaName: String,
)
