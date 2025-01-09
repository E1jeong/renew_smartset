package com.hitec.domain.usecase.main.device_detail

import com.hitec.domain.model.InstallDevice

interface GetInstallDeviceFromImeiUseCase {
    suspend operator fun invoke(
        imei: String,
    ): Result<InstallDevice>
}