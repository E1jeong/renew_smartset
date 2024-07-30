package com.hitec.data.model

import com.hitec.data.model.entity.SubAreaEntity
import com.hitec.domain.model.SubArea
import com.hitec.domain.model.SubAreaModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "SubAreaListReponse") // 오타 주의
data class SubAreaListResponse(
    @PropertyElement(name = "userId") val userId: String,
    @PropertyElement(name = "userLevel") val userLevel: Int,
    @PropertyElement(name = "localSite") val localSite: String,
    @PropertyElement(name = "returnCode") val returnCode: Int,
    @PropertyElement(name = "msgCode") val msgCode: Int,
    @Element(name = "SubAreaInfo") val subAreaInfo: List<SubAreaInfo>
)

@Xml(name = "SubAreaInfo")
data class SubAreaInfo(
    @PropertyElement(name = "rowNumber") val rowNumber: Int,
    @PropertyElement(name = "areaCd") val areaCd: Int,
    @PropertyElement(name = "parentAreaCd") val parentAreaCd: Int,
    @PropertyElement(name = "areaNm") val areaName: String,
)

fun SubAreaListResponse.toDomainModel(): SubArea = SubArea(
    userId = userId,
    userLevel = userLevel,
    localSite = localSite,
    returnCode = returnCode,
    msgCode = msgCode,
    subAreaInfo = subAreaInfo.map { response ->
        SubAreaModel(
            rowNumber = response.rowNumber,
            areaCd = response.areaCd,
            parentAreaCd = response.parentAreaCd,
            areaName = response.areaName
        )
    }
)

fun SubAreaListResponse.toEntity(): List<SubAreaEntity> = subAreaInfo.map { info ->
    SubAreaEntity(
        rowNumber = info.rowNumber,
        areaCd = info.areaCd,
        parentAreaCd = info.parentAreaCd,
        areaName = info.areaName
    )
}