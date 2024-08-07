package com.hitec.data.model

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "DeleteInstallDBReponse") //오타 주의
data class DeleteInstallDBResponse(
    @PropertyElement(name = "msgCode") val msgCode: Int,
)