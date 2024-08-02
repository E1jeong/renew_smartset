package com.hitec.data.usecase

import com.hitec.data.db.importer.SqliteToRoomImporter
import com.hitec.data.db.retrofit.HitecService
import com.hitec.domain.usecase.GetInstallDbUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetInstallDbUseCaseImpl @Inject constructor(
    private val hitecService: HitecService,
    private val databaseImporter: SqliteToRoomImporter,
) : GetInstallDbUseCase {
    override suspend fun invoke(url: String): Result<Unit> = kotlin.runCatching {
        val response = hitecService.getInstallDb(url)

        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                withContext(Dispatchers.IO) {
                    databaseImporter.importDatabase(responseBody)
                }
            } ?: throw Exception("Response body is null")
        } else {
            throw Exception("Failed to download file: ${response.code()}")
        }
    }
}