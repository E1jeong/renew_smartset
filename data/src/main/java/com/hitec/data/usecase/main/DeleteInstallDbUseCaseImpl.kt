package com.hitec.data.usecase.main

import com.hitec.data.db.retrofit.HitecService
import com.hitec.domain.usecase.main.DeleteInstallDbUseCase
import javax.inject.Inject

class DeleteInstallDbUseCaseImpl @Inject constructor(
    private val hitecService: HitecService
) : DeleteInstallDbUseCase {
    override suspend fun invoke(
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        fileName: String,
    ): Result<Unit> = kotlin.runCatching {
        hitecService.deleteInstallDb(
            userId = userId,
            password = password,
            mobileId = mobileId,
            bluetoothId = bluetoothId,
            fileName = fileName
        )
    }
}