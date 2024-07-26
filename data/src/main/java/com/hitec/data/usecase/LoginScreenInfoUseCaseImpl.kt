package com.hitec.data.usecase

import com.hitec.data.db.datastore.LoginScreenInfoPreference
import com.hitec.domain.model.LoginScreenInfo
import com.hitec.domain.usecase.LoginScreenInfoUseCase
import javax.inject.Inject

class LoginScreenInfoUseCaseImpl @Inject constructor(
    private val loginPreference: LoginScreenInfoPreference
) : LoginScreenInfoUseCase {

    override suspend fun saveLoginScreenInfo(
        id: String,
        password: String,
        localSite: String,
        isSwitchOn: Boolean
    ): Result<Unit> = kotlin.runCatching {
        loginPreference.saveLoginScreenInfo(id, password, localSite, isSwitchOn)
    }

    override suspend fun getLoginScreenInfo(): Result<LoginScreenInfo> = kotlin.runCatching {
        loginPreference.getLoginScreenInfo()
    }

    override suspend fun clearLoginScreenInfo(): Result<Unit> = kotlin.runCatching {
        loginPreference.clearLoginScreenInfo()
    }
}