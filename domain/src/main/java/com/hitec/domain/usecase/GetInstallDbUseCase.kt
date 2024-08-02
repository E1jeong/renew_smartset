package com.hitec.domain.usecase

interface GetInstallDbUseCase {

    suspend operator fun invoke(
        url: String
    ): Result<Unit>
}