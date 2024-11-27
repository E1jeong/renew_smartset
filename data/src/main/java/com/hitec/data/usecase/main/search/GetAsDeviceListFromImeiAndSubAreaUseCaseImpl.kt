package com.hitec.data.usecase.main.search

import com.hitec.data.db.room.dao.AsDeviceDao
import com.hitec.data.model.entity.toDomainModel
import com.hitec.domain.model.AsDevice
import com.hitec.domain.usecase.main.search.GetAsDeviceListFromImeiAndSubAreaUseCase
import javax.inject.Inject

class GetAsDeviceListFromImeiAndSubAreaUseCaseImpl @Inject constructor(
    private val asDeviceDao: AsDeviceDao
) : GetAsDeviceListFromImeiAndSubAreaUseCase {
    override suspend fun invoke(
        subArea: String,
        imei: String,
    ): Result<List<AsDevice>> = kotlin.runCatching {
        asDeviceDao.getAsDeviceListWithImeiAndSubArea(subArea, imei).map { it.toDomainModel() }
    }
}