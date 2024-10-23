package com.hitec.data.usecase.main

import com.hitec.data.db.room.dao.InstallDeviceDao
import com.hitec.data.model.entity.toDomainModel
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.usecase.main.GetInstallDeviceUseCase
import javax.inject.Inject

class GetInstallDeviceUseCaseImpl @Inject constructor(
    private val installDeviceDao: InstallDeviceDao
) : GetInstallDeviceUseCase {
    override suspend fun invoke(): Result<List<InstallDevice>> = kotlin.runCatching {
        installDeviceDao.getAll().map { it.toDomainModel() }
    }
}