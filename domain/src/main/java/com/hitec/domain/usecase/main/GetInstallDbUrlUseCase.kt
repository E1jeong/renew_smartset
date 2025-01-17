package com.hitec.domain.usecase.main

interface GetInstallDbUrlUseCase {

    suspend operator fun invoke(
//        method: String, 해당 부분은 retrofit service에서 하드코딩 되어있음
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String,
        areaCd: String = "",
        reqAsCd: String = "",
        installGroupCd: String = "",
        connectionType: String = "",
    ): Result<String>
}
