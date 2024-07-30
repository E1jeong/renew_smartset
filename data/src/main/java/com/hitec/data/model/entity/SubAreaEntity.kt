package com.hitec.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "T_SubArea")
data class SubAreaEntity(
    @PrimaryKey val rowNumber: Int,
    val areaCd: Int,
    val parentAreaCd: Int,
    val areaName: String,
)
