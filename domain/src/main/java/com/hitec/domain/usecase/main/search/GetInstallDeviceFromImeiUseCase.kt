package com.hitec.domain.usecase.main.search

import com.hitec.domain.model.InstallDevice

interface GetInstallDeviceFromImeiUseCase {
    suspend operator fun invoke(
        imei: String,
    ): Result<InstallDevice>
}