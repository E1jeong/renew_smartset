package com.hitec.data.usecase.main.device_detail

import com.hitec.data.db.room.dao.InstallDeviceDao
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.usecase.main.device_detail.GetInstallDeviceFromImeiUseCase
import javax.inject.Inject

class GetInstallDeviceFromImeiUseCaseImpl @Inject constructor(
    private val installDeviceDao: InstallDeviceDao,
) : GetInstallDeviceFromImeiUseCase {
    override suspend fun invoke(imei: String): Result<InstallDevice> = kotlin.runCatching {
        installDeviceDao.getDeviceWithImei(imei)
    }
}