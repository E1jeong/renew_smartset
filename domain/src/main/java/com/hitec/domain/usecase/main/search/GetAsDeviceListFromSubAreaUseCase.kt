package com.hitec.domain.usecase.main.search

import com.hitec.domain.model.AsDevice

interface GetAsDeviceListFromSubAreaUseCase {
    suspend operator fun invoke(
        subArea: String,
    ): Result<List<AsDevice>>
}
