package com.hitec.domain.usecase.main.device_detail

import com.hitec.domain.model.InstallDevice

interface UpdateInstallDeviceUseCase {

    suspend operator fun invoke(installDevice: InstallDevice): Result<Unit>
}