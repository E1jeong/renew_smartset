package com.hitec.data.usecase.main

import com.hitec.data.db.room.dao.ServerInfoDao
import com.hitec.data.model.entity.toDomainModel
import com.hitec.domain.model.ServerInfo
import com.hitec.domain.usecase.main.GetServerInfoUseCase
import javax.inject.Inject

class GetServerInfoUseCaseImpl @Inject constructor(
    private val serverInfoDao: ServerInfoDao,
) : GetServerInfoUseCase {
    override suspend fun invoke(): Result<ServerInfo> = kotlin.runCatching {
        serverInfoDao.getAll().first().toDomainModel()
    }
}