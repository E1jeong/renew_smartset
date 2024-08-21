package com.hitec.data.usecase

import com.hitec.data.db.room.dao.InstallDeviceDao
import com.hitec.data.model.entity.toDomainModel
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.usecase.main.search.GetInstallDeviceListFromSubAreaUseCase
import javax.inject.Inject

class GetInstallDeviceListFromSubAreaUseCaseImpl @Inject constructor(
    private val installDeviceDao: InstallDeviceDao,
) : GetInstallDeviceListFromSubAreaUseCase {
    override suspend fun invoke(
        subArea: String,
    ): Result<List<InstallDevice>> = kotlin.runCatching {
        installDeviceDao.getDeviceListWithSubArea(subArea).map { it.toDomainModel() }
    }
}