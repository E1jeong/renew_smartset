package com.hitec.data.usecase.main

import com.hitec.data.db.room.dao.AsDeviceDao
import com.hitec.data.model.entity.toDomainModel
import com.hitec.domain.model.AsDevice
import com.hitec.domain.usecase.main.GetAsDeviceUseCase
import javax.inject.Inject

class GetAsDeviceUseCaseImpl @Inject constructor(
    private val asDeviceDao: AsDeviceDao
) : GetAsDeviceUseCase {
    override suspend fun invoke(): Result<List<AsDevice>> = kotlin.runCatching {
        asDeviceDao.getAll()
            .filter { it.deviceSn != null }
            .map { it.toDomainModel() }
    }
}