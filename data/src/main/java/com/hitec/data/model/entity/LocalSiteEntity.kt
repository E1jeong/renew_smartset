package com.hitec.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hitec.domain.model.LocalSiteModel

@Entity(tableName = "T_LocalSite")
data class LocalSiteEntity(
    @PrimaryKey val rowNumber: Int,
    val siteNameEng: String,
    val siteNameKor: String,
    val connectionType: Int,
    val vpnSecuwayVersion: Int,
)

fun LocalSiteEntity.toDomainModel() = LocalSiteModel(
    rowNumber = rowNumber,
    siteId = siteNameEng,
    siteName = siteNameKor,
    connectionType = connectionType,
    vpnSecuwayVersion = vpnSecuwayVersion,
)