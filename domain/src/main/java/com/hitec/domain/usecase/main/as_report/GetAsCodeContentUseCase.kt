package com.hitec.domain.usecase.main.as_report

import com.hitec.domain.model.AsCode

interface GetAsCodeUseCase {

    suspend operator fun invoke(groupId: String?): Result<List<AsCode>>
}