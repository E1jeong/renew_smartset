package com.hitec.data.usecase

import com.hitec.data.db.room.dao.LocalSiteDao
import com.hitec.data.model.entity.toDomainModel
import com.hitec.domain.model.LocalSiteModel
import com.hitec.domain.usecase.FindLocalSiteNameUseCase
import javax.inject.Inject

class FindLocalSiteNameUseCaseImpl @Inject constructor(
    private val localSiteDao: LocalSiteDao
) : FindLocalSiteNameUseCase {
    override suspend fun invoke(siteName: String): Result<List<LocalSiteModel>> =
        kotlin.runCatching {
            localSiteDao.findLocalSiteName(siteName).map { it.toDomainModel() }
        }
}