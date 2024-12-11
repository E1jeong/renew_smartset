package com.hitec.domain.usecase.main.as_report

import com.hitec.domain.model.AsDevice

interface UpdateAsDeviceUseCase {
    suspend operator fun invoke(asDevice: AsDevice): Result<Unit>
}