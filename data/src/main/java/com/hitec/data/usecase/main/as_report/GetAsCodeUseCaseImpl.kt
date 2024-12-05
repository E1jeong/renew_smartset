package com.hitec.data.usecase.main.as_report

import com.hitec.data.db.room.dao.AsCodeDao
import com.hitec.domain.model.AsCode
import com.hitec.domain.usecase.main.as_report.GetAsCodeUseCase
import javax.inject.Inject

class GetAsCodeUseCaseImpl @Inject constructor(
    private val asCodeDao: AsCodeDao
) : GetAsCodeUseCase {
    override suspend fun invoke(groupId: String?): Result<List<AsCode>> = kotlin.runCatching {
        asCodeDao.getAsCode(groupId ?: "CC34").map { it.toDomainModel() }
    }
}