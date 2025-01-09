package com.hitec.domain.usecase.main.as_report

import com.hitec.domain.model.AsDevice

interface GetAsDeviceFromImeiUseCase {
    suspend operator fun invoke(
        imei: String,
    ): Result<AsDevice>
}
