package com.hitec.data.usecase.main.search

import com.hitec.data.db.room.dao.AsDeviceDao
import com.hitec.data.model.entity.toDomainModel
import com.hitec.domain.model.AsDevice
import com.hitec.domain.usecase.main.search.GetAsDeviceListFromSubAreaUseCase
import javax.inject.Inject

class GetAsDeviceListFromSubAreaUseCaseImpl @Inject constructor(
    private val asDeviceDao: AsDeviceDao,
) : GetAsDeviceListFromSubAreaUseCase {

    override suspend fun invoke(
        subArea: String,
    ): Result<List<AsDevice>> = kotlin.runCatching {
        asDeviceDao.getAsDeviceListWithSubArea(subArea).map { it.toDomainModel() }
    }
}