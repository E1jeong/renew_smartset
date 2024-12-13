package com.hitec.domain.usecase.main.device_detail

import com.hitec.domain.model.UploadInstallEssential

interface PostUploadInstallEssentialUseCase {

    suspend operator fun invoke(
//        method: String, 해당 부분은 retrofit service에서 하드코딩 되어있음
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String,

        //down to body param
        cdmaNo: String,
        nwk: String,
        firmware: String,
        serverName: String,
        serverSite: String,
        consumeHouseNo: String,
        deviceSn: String,
    ): Result<UploadInstallEssential>
}