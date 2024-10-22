package com.hitec.data.usecase.main.device_detail

import com.hitec.data.db.room.dao.InstallDeviceDao
import com.hitec.data.model.entity.toEntity
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.usecase.main.device_detail.UpdateInstallDeviceUseCase
import javax.inject.Inject

class UpdateInstallDeviceUseCaseImpl @Inject constructor(
    val installDeviceDao: InstallDeviceDao
) : UpdateInstallDeviceUseCase {
    override suspend fun invoke(installDevice: InstallDevice): Result<Unit> = kotlin.runCatching {
        installDeviceDao.updateInstallDevice(installDevice.toEntity())
    }
}