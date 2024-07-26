package com.hitec.domain.usecase

import com.hitec.domain.model.LoginScreenInfo

interface LoginScreenInfoUseCase {

    suspend fun saveLoginScreenInfo(
        id: String,
        password: String,
        localSite: String,
        isSwitchOn: Boolean
    ): Result<Unit>

    suspend fun getLoginScreenInfo(): Result<LoginScreenInfo>

    suspend fun clearLoginScreenInfo(): Result<Unit>
}