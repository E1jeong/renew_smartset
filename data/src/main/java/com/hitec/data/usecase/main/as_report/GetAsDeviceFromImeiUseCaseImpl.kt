package com.hitec.data.usecase.main.as_report

import com.hitec.data.db.room.dao.AsDeviceDao
import com.hitec.domain.model.AsDevice
import com.hitec.domain.usecase.main.as_report.GetAsDeviceFromImeiUseCase
import javax.inject.Inject

class GetAsDeviceFromImeiUseCaseImpl @Inject constructor(
    private val asDeviceDao: AsDeviceDao
) : GetAsDeviceFromImeiUseCase {
    override suspend fun invoke(imei: String): Result<AsDevice> = kotlin.runCatching {
        asDeviceDao.getDeviceWithImei(imei)
    }
}