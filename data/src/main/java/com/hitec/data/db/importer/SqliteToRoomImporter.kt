package com.hitec.data.db.importer

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.withTransaction
import com.hitec.data.db.room.ApplicationDatabase
import com.hitec.data.db.room.dao.InstallDeviceDao
import com.hitec.data.db.room.dao.ServerInfoDao
import com.hitec.data.model.entity.InstallDeviceEntity
import com.hitec.data.model.entity.ServerInfoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

class SqliteToRoomImporter @Inject constructor(
    private val context: Context,
    private val database: ApplicationDatabase,
    private val serverInfoDao: ServerInfoDao,
    private val installDeviceDao: InstallDeviceDao,
) {
    suspend fun importDatabase(responseBody: ResponseBody) {
        val tempDbFile = saveTempDatabase(responseBody)
        importTables(tempDbFile)
        tempDbFile.delete()
    }

    private fun saveTempDatabase(responseBody: ResponseBody): File {
        val file = File(context.filesDir, "temp_db.sqlite")
        file.outputStream().use { fileOut ->
            responseBody.byteStream().copyTo(fileOut)
        }
        return file
    }

    private suspend fun importTables(sqliteDBFile: File) {
        SQLiteDatabase.openDatabase(sqliteDBFile.absolutePath, null, SQLiteDatabase.OPEN_READONLY)
            .use { tempDb ->
                importServerInfo(tempDb)
                importInstallDevice(tempDb)
            }
    }

    private suspend fun importServerInfo(tempDb: SQLiteDatabase) {
        val sysinfoQuery = "SELECT * FROM $SQLITE_TABLE_SYSINFO"
        val serverQuery = "SELECT * FROM $SQLITE_TABLE_SERVER"

        val sysinfoEntities = tempDb.rawQuery(sysinfoQuery, null).use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map { mapSysinfoToServerInfo(it) }
                .toList()
        }

        val serverEntities = tempDb.rawQuery(serverQuery, null).use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map { mapServerToServerInfo(it) }
                .toList()
        }

        val mergedEntities = (sysinfoEntities + serverEntities)
            .groupBy { it.nbServiceCode ?: "" }
            .map { (key, entities) ->
                entities.reduce { acc, entity ->
                    acc.merge(entity).copy(nbServiceCode = key.ifEmpty { null })
                }
            }

        serverInfoDao.insert(mergedEntities)
    }

    private suspend fun importInstallDevice(tempDb: SQLiteDatabase) {
        val query = "SELECT * FROM $SQLITE_TABLE_INSTALL"

        val entityCount = tempDb.rawQuery(query, null).use { cursor ->
            cursor.count
        }
        Log.d("DatabaseImport", "Entity count from SQLite: $entityCount")

        val entities = tempDb.rawQuery(query, null).use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map { mapToInstallDeviceEntity(it) }
                .onEach { Log.d("DatabaseImport", "Mapped entity: $it") }
                .toList()
        }
        Log.d("DatabaseImport", "Total mapped entities: ${entities.size}")


        withContext(Dispatchers.IO) {
            database.withTransaction {
                installDeviceDao.delete()
                installDeviceDao.insert(entities)

                val insertedCount = installDeviceDao.insert(entities).size
                Log.d("DatabaseImport", "Inserted entity count: $insertedCount")
            }
        }
    }

    private fun mapSysinfoToServerInfo(cursor: Cursor): ServerInfoEntity {
        return ServerInfoEntity(
            nbServiceCode = cursor.getStringOrNull("nbServiceCode") ?: "",
            dbVersion = cursor.getIntOrNull("dbVersion"),
            meterManPwd = cursor.getStringOrNull("meterManPwd"),
            serverName = cursor.getStringOrNull("serverName"),
            serverSite = cursor.getStringOrNull("serverSite"),
            asSiteId = cursor.getIntOrNull("asSiteId")
        )
    }

    private fun mapServerToServerInfo(cursor: Cursor): ServerInfoEntity {
        return ServerInfoEntity(
            nbServiceCode = cursor.getStringOrNull("nbServiceCode") ?: "",
            localGoverName = cursor.getStringOrNull("localGoverName"),
            serverIP = cursor.getStringOrNull("serverIP"),
            serverPort = cursor.getIntOrNull("serverPort"),
            serverURL = cursor.getStringOrNull("serverURL"),
            serverConnectionCode = cursor.getIntOrNull("serverConnectionCode"),
            nbServerIp = cursor.getStringOrNull("nbServerIp"),
            nbServerPort = cursor.getIntOrNull("nbServerPort")
        )
    }

    private fun mapToInstallDeviceEntity(cursor: Cursor): InstallDeviceEntity {
        return InstallDeviceEntity(
            modTypeCd = cursor.getStringOrNull("modTypeCd") ?: "",
            meterDeviceId = cursor.getStringOrNull("meterDeviceId") ?: "",
            deviceTypeCd = cursor.getStringOrNull("deviceTypeCd") ?: "",
            consumeHouseNo = cursor.getStringOrNull("consumeHouseNo") ?: "",
            meterMethodCd = cursor.getStringOrNull("meterMethodCd") ?: "",
            deviceModelCd = cursor.getStringOrNull("deviceModelCd") ?: "",
            companyCd = cursor.getStringOrNull("companyCd") ?: "",
            meterDeviceNm = cursor.getStringOrNull("meterDeviceNm") ?: "",
            meterDeviceSn = cursor.getStringOrNull("meterDeviceSn") ?: "",
            consumeStateCd = cursor.getStringOrNull("consumeStateCd") ?: "",
            deviceStateCd = cursor.getStringOrNull("deviceStateCd") ?: "",
            meterStateCd1 = cursor.getStringOrNull("meterStateCd1") ?: "",
            meterStateCd2 = cursor.getStringOrNull("meterStateCd2") ?: "",
            meterStateCd3 = cursor.getStringOrNull("meterStateCd3") ?: "",
            installGroupCd = cursor.getStringOrNull("installGroupCd") ?: "",
            uploadResultCd = cursor.getStringOrNull("uploadResultCd") ?: "",
            uploadErrorCd = cursor.getStringOrNull("uploadErrorCd") ?: "",
            barcode = cursor.getStringOrNull("barcode") ?: "",
            cameraSave = cursor.getStringOrNull("cameraSave") ?: "",
            imageUpload = cursor.getStringOrNull("imageUpload") ?: "",
            setDt = cursor.getStringOrNull("setDt") ?: "",
            AreaBig = cursor.getStringOrNull("AreaBig") ?: "",
            AreaBigCd = cursor.getStringOrNull("AreaBigCd") ?: "",
            AreaMid = cursor.getStringOrNull("AreaMid") ?: "",
            AreaMidCd = cursor.getStringOrNull("AreaMidCd") ?: "",
            AreaSmall = cursor.getStringOrNull("AreaSmall") ?: "",
            AreaSmallCd = cursor.getStringOrNull("AreaSmallCd") ?: "",
            setAreaAddr = cursor.getStringOrNull("setAreaAddr") ?: "",
            setPlaceDesc = cursor.getStringOrNull("setPlaceDesc") ?: "",
            cdmaNo = cursor.getStringOrNull("cdmaNo") ?: "",
            serverAddr1 = cursor.getStringOrNull("serverAddr1") ?: "",
            serverPort1 = cursor.getStringOrNull("serverPort1") ?: "",
            pan = cursor.getStringOrNull("pan") ?: "",
            panNm = cursor.getStringOrNull("panNm") ?: "",
            nwk = cursor.getStringOrNull("nwk") ?: "",
            pnwk = cursor.getStringOrNull("pnwk") ?: "",
            mac = cursor.getStringOrNull("mac") ?: "",
            gpsLatitude = cursor.getStringOrNull("gpsLatitude") ?: "",
            gpsLongitude = cursor.getStringOrNull("gpsLongitude") ?: "",
            meterBaseTime = cursor.getStringOrNull("meterBaseTime") ?: "",
            meterIntervalTime = cursor.getStringOrNull("meterIntervalTime") ?: "",
            reportIntervalTime = cursor.getStringOrNull("reportIntervalTime") ?: "",
            meterStoreMonth = cursor.getStringOrNull("meterStoreMonth") ?: "",
            meterBaseDay = cursor.getStringOrNull("meterBaseDay") ?: "",
            meterAlertTime = cursor.getStringOrNull("meterAlertTime") ?: "",
            meterPeriodTime = cursor.getStringOrNull("meterPeriodTime") ?: "",
            activeStartDay = cursor.getStringOrNull("activeStartDay") ?: "",
            activeEndDay = cursor.getStringOrNull("activeEndDay") ?: "",
            activeStartHour = cursor.getStringOrNull("activeStartHour") ?: "",
            activeEndHour = cursor.getStringOrNull("activeEndHour") ?: "",
            terminalType = cursor.getStringOrNull("terminalType") ?: "",
            firmware = cursor.getStringOrNull("firmware") ?: "",
            subUpdateInterval = cursor.getStringOrNull("subUpdateInterval") ?: "",
            subNwkID = cursor.getStringOrNull("subNwkID") ?: "",
            terminalTypeCd = cursor.getStringOrNull("terminalTypeCd") ?: "",
            consumeHouseNm = cursor.getStringOrNull("consumeHouseNm") ?: "",
            utilityCode = cursor.getStringOrNull("utilityCode") ?: "",
            waterCompCode = cursor.getStringOrNull("waterCompCode") ?: "",
            wmuManufacturerCode = cursor.getStringOrNull("wmuManufacturerCode") ?: "",
            wmuInstallCode = cursor.getStringOrNull("wmuInstallCode") ?: "",
            cdmaTypeCd = cursor.getStringOrNull("cdmaTypeCd") ?: "",
            firmwareGateway = cursor.getStringOrNull("firmwareGateway") ?: "",
            serverConnectionCode = cursor.getStringOrNull("serverConnectionCode") ?: "",
            concenIP = cursor.getStringOrNull("concenIP") ?: "",
            concenGwIP = cursor.getStringOrNull("concenGwIP") ?: "",
            serverURL = cursor.getStringOrNull("serverURL") ?: "",
            rfChn = cursor.getStringOrNull("rfChn") ?: "",
            hcuId = cursor.getStringOrNull("hcuId") ?: "",
            setInitDate = cursor.getStringOrNull("setInitDate") ?: "",
            masterSn = cursor.getStringOrNull("masterSn") ?: "",
            subSn1 = cursor.getStringOrNull("subSn1") ?: "",
            subSn2 = cursor.getStringOrNull("subSn2") ?: "",
            subSn3 = cursor.getStringOrNull("subSn3") ?: "",
            subSn4 = cursor.getStringOrNull("subSn4") ?: "",
            accountMeterUse1 = cursor.getStringOrNull("accountMeterUse1") ?: "",
            accountMeterUse2 = cursor.getStringOrNull("accountMeterUse2") ?: "",
            accountMeterUse3 = cursor.getStringOrNull("accountMeterUse3") ?: "",
            meterCount = cursor.getStringOrNull("meterCount") ?: "",
            meterCd1 = cursor.getStringOrNull("meterCd1") ?: "",
            meterTypeCd1 = cursor.getStringOrNull("meterTypeCd1") ?: "",
            meterPort1 = cursor.getStringOrNull("meterPort1") ?: "",
            meterCompany1 = cursor.getStringOrNull("meterCompany1") ?: "",
            meterSn1 = cursor.getStringOrNull("meterSn1") ?: "",
            meterCurrVal1 = cursor.getStringOrNull("meterCurrVal1") ?: "",
            meterCaliber1 = cursor.getStringOrNull("meterCaliber1") ?: "",
            metercaliberCd1 = cursor.getStringOrNull("metercaliberCd1") ?: "",
            meterDigits1 = cursor.getStringOrNull("meterDigits1") ?: "",
            meterCd2 = cursor.getStringOrNull("meterCd2") ?: "",
            meterTypeCd2 = cursor.getStringOrNull("meterTypeCd2") ?: "",
            meterPort2 = cursor.getStringOrNull("meterPort2") ?: "",
            meterCompany2 = cursor.getStringOrNull("meterCompany2") ?: "",
            meterSn2 = cursor.getStringOrNull("meterSn2") ?: "",
            meterCurrVal2 = cursor.getStringOrNull("meterCurrVal2") ?: "",
            meterCaliber2 = cursor.getStringOrNull("meterCaliber2") ?: "",
            metercaliberCd2 = cursor.getStringOrNull("metercaliberCd2") ?: "",
            meterDigits2 = cursor.getStringOrNull("meterDigits2") ?: "",
            meterCd3 = cursor.getStringOrNull("meterCd3") ?: "",
            meterTypeCd3 = cursor.getStringOrNull("meterTypeCd3") ?: "",
            meterPort3 = cursor.getStringOrNull("meterPort3") ?: "",
            meterCompany3 = cursor.getStringOrNull("meterCompany3") ?: "",
            meterSn3 = cursor.getStringOrNull("meterSn3") ?: "",
            meterCurrVal3 = cursor.getStringOrNull("meterCurrVal3") ?: "",
            meterCaliber3 = cursor.getStringOrNull("meterCaliber3") ?: "",
            metercaliberCd3 = cursor.getStringOrNull("metercaliberCd3") ?: "",
            meterDigits3 = cursor.getStringOrNull("meterDigits3") ?: "",
            communicationTypeCd = cursor.getStringOrNull("communicationTypeCd") ?: "",
            telecomTypeCd = cursor.getStringOrNull("telecomTypeCd") ?: "",
            nbServiceCode = cursor.getStringOrNull("nbServiceCode") ?: "",
            nbCseId = cursor.getStringOrNull("nbCseId") ?: "",
            nbIccId = cursor.getStringOrNull("nbIccId") ?: "",
            accountCheckNote = cursor.getStringOrNull("accountCheckNote") ?: "",
            reportRangeTime = cursor.getStringOrNull("reportRangeTime") ?: "",
            dataSkipFlag = cursor.getStringOrNull("dataSkipFlag") ?: "",
            deviceMemo = cursor.getStringOrNull("deviceMemo") ?: ""
        )
    }

    companion object {
        const val SQLITE_TABLE_INSTALL = "INSTALL"
        const val SQLITE_TABLE_SERVER = "SERVER"
        const val SQLITE_TABLE_SYSINFO = "SYSINFO"
    }
}

private fun Cursor.getStringOrNull(columnName: String): String? {
    return getColumnIndex(columnName).takeIf { it >= 0 }?.let { getString(it) }
}

private fun Cursor.getIntOrNull(columnName: String): Int? {
    return getColumnIndex(columnName).takeIf { it >= 0 }?.let { getInt(it) }
}