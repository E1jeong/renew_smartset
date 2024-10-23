package com.hitec.data.usecase.main

import com.hitec.data.db.retrofit.HitecService
import com.hitec.domain.usecase.main.GetInstallDbUrlUseCase
import javax.inject.Inject

class GetInstallDbUrlUseCaseImpl @Inject constructor(
    private val hitecService: HitecService,
) : GetInstallDbUrlUseCase {
    override suspend fun invoke(
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String,
        areaCd: String,
        reqAsCd: String,
        installGroupCd: String,
        connectionType: String
    ): Result<String> = kotlin.runCatching {
        val response = hitecService.getInstallDbUrl(
            userId = userId,
            password = password,
            mobileId = mobileId,
            bluetoothId = bluetoothId,
            localSite = localSite
        )

        response.installDBInfo.map { it.siteUrl }.first()
    }
}