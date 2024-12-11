package com.hitec.domain.usecase.main.as_report

import com.hitec.domain.model.UploadAsEssential

/*
해당 수용가에 대해 사진이 서버에 있나 없나 판단 하는 기능
 */
interface PostUploadAsEssentialUseCase {

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
        fieldActionMain: String,
        fieldActionSub: String,
        consumeHouseNo: String,
        deviceSn: String,
    ): Result<UploadAsEssential>
}