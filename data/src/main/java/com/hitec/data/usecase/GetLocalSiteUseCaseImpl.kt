package com.hitec.data.usecase

import com.hitec.data.db.retrofit.HitecService
import com.hitec.data.db.room.dao.LocalSiteDao
import com.hitec.data.model.entity.LocalSiteEntity
import com.hitec.data.model.toDomainModel
import com.hitec.data.model.toEntity
import com.hitec.domain.model.LocalSite
import com.hitec.domain.usecase.GetLocalSiteUseCase
import javax.inject.Inject

class GetLocalSiteUseCaseImpl @Inject constructor(
    private val hitecService: HitecService,
    private val localSiteDao: LocalSiteDao
) : GetLocalSiteUseCase {

    override suspend fun invoke(
//        method: String, 해당 부분은 retrofit service에서 하드코딩 되어있음
        userId: String,
        password: String,
        mobileId: String,
        bluetoothId: String,
    ): Result<LocalSite> = kotlin.runCatching {
        val localSiteResponse = hitecService.getLocalSite(
            userId = userId,
            password = password,
            mobileId = mobileId,
            bluetoothId = bluetoothId
        )
        val entities: List<LocalSiteEntity> = localSiteResponse.toEntity()
        localSiteDao.insert(entities)

        localSiteResponse.toDomainModel()
    }
}
