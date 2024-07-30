package com.hitec.domain.model

data class LoginScreenInfo(
    val id: String,
    val password: String,
    val localSite: String,
    val androidDeviceId: String,
    val isSwitchOn: Boolean
)