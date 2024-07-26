package com.hitec.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "T_LocalSite")
data class LocalSiteEntity(
    @PrimaryKey val rowNumber: Int,
    val siteNameEng: String,
    val siteNameKor: String,
    val connectionType: Int,
    val vpnSecuwayVersion: Int,
)