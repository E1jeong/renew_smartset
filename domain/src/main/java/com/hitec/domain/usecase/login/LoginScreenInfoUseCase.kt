package com.hitec.domain.usecase.login

import com.hitec.domain.model.LoginScreenInfo

interface LoginScreenInfoUseCase {

    suspend fun saveLoginScreenInfo(
        id: String,
        password: String,
        localSite: String,
        androidDeviceId: String,
        isSwitchOn: Boolean,
        localSiteEngWrittenByUser: String,
    ): Result<Unit>

    suspend fun getLoginScreenInfo(): Result<LoginScreenInfo>

    suspend fun clearLoginScreenInfo(): Result<Unit>
}