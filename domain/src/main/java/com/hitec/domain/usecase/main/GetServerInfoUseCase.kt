package com.hitec.domain.usecase.main

import com.hitec.domain.model.ServerInfo

interface GetServerInfoUseCase {

    suspend operator fun invoke(): Result<ServerInfo>
}