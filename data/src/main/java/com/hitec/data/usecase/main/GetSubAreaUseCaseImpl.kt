package com.hitec.data.usecase.main

import com.hitec.data.db.retrofit.HitecService
import com.hitec.data.db.room.dao.SubAreaDao
import com.hitec.data.model.entity.SubAreaEntity
import com.hitec.data.model.toDomainModel
import com.hitec.data.model.toEntity
import com.hitec.domain.model.SubArea
import com.hitec.domain.usecase.main.GetSubAreaUseCase
import javax.inject.Inject

class GetSubAreaUseCaseImpl @Inject constructor(
    private val hitecService: HitecService,
    private val subAreaDao: SubAreaDao
) : GetSubAreaUseCase {

    override suspend fun invoke(
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
        localSite: String
    ): Result<SubArea> = kotlin.runCatching {
        val response = hitecService.getSubArea(
            userId = userId,
            password = password,
            mobileId = mobileId,
            bluetoothId = bluetoothId,
            localSite = localSite
        )

        val entities: List<SubAreaEntity> = response.toEntity()
        subAreaDao.insert(entities)

        response.toDomainModel()
    }
}