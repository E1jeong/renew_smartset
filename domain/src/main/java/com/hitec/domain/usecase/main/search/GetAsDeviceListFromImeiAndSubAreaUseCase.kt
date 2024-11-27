package com.hitec.domain.usecase.main.search

import com.hitec.domain.model.AsDevice

interface GetAsDeviceListFromImeiAndSubAreaUseCase {

    //SearchViewModel에서 state의 default값이 null이 아니라 "" 이기때문에 null safe를 표시하지 않음 (String?)
    suspend operator fun invoke(
        subArea: String,
        imei: String,
    ): Result<List<AsDevice>>
}

