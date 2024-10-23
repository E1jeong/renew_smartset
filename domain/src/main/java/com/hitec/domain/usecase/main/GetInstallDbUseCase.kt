package com.hitec.domain.usecase.main

interface GetInstallDbUseCase {

    suspend operator fun invoke(
        url: String
    ): Result<Unit>
}