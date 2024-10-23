package com.hitec.domain.usecase.login

import com.hitec.domain.model.LocalSiteModel

interface FindLocalSiteNameUseCase {

    suspend operator fun invoke(siteName: String): Result<List<LocalSiteModel>>
}