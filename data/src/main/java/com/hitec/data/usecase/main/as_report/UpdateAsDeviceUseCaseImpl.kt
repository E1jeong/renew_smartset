package com.hitec.data.usecase.main.as_report

import com.hitec.data.db.room.dao.AsDeviceDao
import com.hitec.data.model.entity.toEntity
import com.hitec.domain.model.AsDevice
import com.hitec.domain.usecase.main.as_report.UpdateAsDeviceUseCase
import javax.inject.Inject

class UpdateAsDeviceUseCaseImpl @Inject constructor(
    private val asDeviceDao: AsDeviceDao
) : UpdateAsDeviceUseCase {
    override suspend fun invoke(asDevice: AsDevice): Result<Unit> = kotlin.runCatching {
        asDeviceDao.update(asDevice.toEntity())
    }
}