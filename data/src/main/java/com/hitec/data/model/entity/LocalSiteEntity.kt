package com.hitec.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

data class RoomLocalSite(
    val userId: String,
    val userLevel: Int,
    val localSite: String,
    val returnCode: Int,
    val msgCode: Int,
    val localSiteInfo: List<LocalSiteEntity>,
)

@Entity(tableName = "T_LocalSite")
data class LocalSiteEntity(
    @PrimaryKey val rowNumber: Int,
    val siteNameEng: String,
    val siteNameKor: String,
    val connectionType: Int,
    val vpnSecuwayVersion: Int,
)