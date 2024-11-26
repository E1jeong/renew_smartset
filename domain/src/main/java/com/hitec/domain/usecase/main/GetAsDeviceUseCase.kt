package com.hitec.domain.usecase.main

import com.hitec.domain.model.AsDevice

interface GetAsDeviceUseCase {

    suspend operator fun invoke(): Result<List<AsDevice>>
}