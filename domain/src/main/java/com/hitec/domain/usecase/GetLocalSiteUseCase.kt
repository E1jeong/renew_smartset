package com.hitec.domain.usecase

import com.hitec.domain.model.LocalSite

interface GetLocalSiteUseCase {

    suspend operator fun invoke(
//        method: String,
//        userId: String,
//        password: String,
//        mobileId: String,
//        bluetoothId: String,
    ): Result<LocalSite>
}