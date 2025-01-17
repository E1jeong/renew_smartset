package com.hitec.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hitec.domain.model.AsCode

@Entity(tableName = "T_AsCode")
data class AsCodeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "CD_GROUP_ID") val asCodeGroupId: String? = null,
    @ColumnInfo(name = "CD_ID") val asCodeId: String? = null,
    @ColumnInfo(name = "CD_ID_SUB") val asCodeSubId: String? = null,
    @ColumnInfo(name = "CD_NM") val asCodeName: String? = null,
    @ColumnInfo(name = "CD_SUBNM") val asCodeSubName: String? = null,
    @ColumnInfo(name = "CD_FIELD_ACTION_MAIN") val asCodeFieldActionMain: String? = null,
) {
    fun toDomainModel(): AsCode {
        return AsCode(
            asCodeGroupId = this.asCodeGroupId,
            asCodeId = this.asCodeId,
            asCodeSubId = this.asCodeSubId,
            asCodeName = this.asCodeName,
            asCodeSubName = this.asCodeSubName,
            asCodeFieldActionMain = this.asCodeFieldActionMain
        )
    }
}