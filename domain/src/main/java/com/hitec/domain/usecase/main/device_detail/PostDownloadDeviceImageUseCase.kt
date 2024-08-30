package com.hitec.domain.usecase.main.device_detail

import com.hitec.domain.model.DeviceImage

interface PostDownloadDeviceImageUseCase {

    suspend operator fun invoke(
//        method: String, 해당 부분은 retrofit service에서 하드코딩 되어있음
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String,
        meterDeviceId: String, // body param
        deviceTypeCd: String, // body param
        meterCd: String?, // body param
        photoTypeCd: Int, // body param
    ): Result<DeviceImage>
}