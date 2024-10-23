package com.hitec.data.usecase.main.search

import com.hitec.data.db.room.dao.InstallDeviceDao
import com.hitec.data.model.entity.toDomainModel
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.usecase.main.search.GetInstallDeviceListFromImeiAndSubAreaUseCase
import javax.inject.Inject

class GetInstallDeviceListFromImeiAndSubAreaUseCaseImpl @Inject constructor(
    private val installDeviceDao: InstallDeviceDao,
) : GetInstallDeviceListFromImeiAndSubAreaUseCase {
    override suspend fun invoke(
        subArea: String,
        imei: String,
    ): Result<List<InstallDevice>> = kotlin.runCatching {
        installDeviceDao.getDeviceListWithImeiAndSubArea(subArea, imei).map { it.toDomainModel() }
    }
}