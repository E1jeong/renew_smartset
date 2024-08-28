package com.hitec.domain.usecase.main.device_detail

import com.hitec.domain.model.DownloadableImageList

/*
해당 수용가에 대해 사진이 서버에 있나 없나 판단 하는 기능
 */
interface PostDownloadableImageListUseCase {

    suspend operator fun invoke(
//        method: String, 해당 부분은 retrofit service에서 하드코딩 되어있음
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String,
        meterDeviceId: String, // body param
        deviceTypeCd: String, // body param
    ): Result<List<DownloadableImageList>>
}