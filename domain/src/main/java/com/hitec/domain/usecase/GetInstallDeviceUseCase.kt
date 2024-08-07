package com.hitec.domain.usecase

import com.hitec.domain.model.InstallDevice

interface GetInstallDeviceUseCase {

    suspend operator fun invoke(): Result<List<InstallDevice>>
}