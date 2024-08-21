package com.hitec.domain.usecase.main.search

import com.hitec.domain.model.InstallDevice

interface GetInstallDeviceListFromSubAreaUseCase {

    suspend operator fun invoke(
        subArea: String,
    ): Result<List<InstallDevice>>
}