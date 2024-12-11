package com.hitec.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hitec.domain.model.ServerInfo

@Entity(tableName = "T_ServerInfo")
data class ServerInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nbServiceCode: String? = null,
    val dbVersion: Int? = null,
    val meterManPwd: String? = null,
    val serverName: String? = null,
    val serverSite: String? = null,
    val asSiteId: Int? = null,
    val localGoverName: String? = null,
    val serverIP: String? = null,
    val serverPort: Int? = null,
    val serverURL: String? = null,
    val serverConnectionCode: Int? = null,
    val nbServerIp: String? = null,
    val nbServerPort: Int? = null
) {
    fun merge(other: ServerInfoEntity): ServerInfoEntity {
        return ServerInfoEntity(
            nbServiceCode = this.nbServiceCode,
            dbVersion = this.dbVersion ?: other.dbVersion,
            meterManPwd = this.meterManPwd ?: other.meterManPwd,
            serverName = this.serverName ?: other.serverName,
            serverSite = this.serverSite ?: other.serverSite,
            asSiteId = this.asSiteId ?: other.asSiteId,
            localGoverName = this.localGoverName ?: other.localGoverName,
            serverIP = this.serverIP ?: other.serverIP,
            serverPort = this.serverPort ?: other.serverPort,
            serverURL = this.serverURL ?: other.serverURL,
            serverConnectionCode = this.serverConnectionCode ?: other.serverConnectionCode,
            nbServerIp = this.nbServerIp ?: other.nbServerIp,
            nbServerPort = this.nbServerPort ?: other.nbServerPort
        )
    }
}

fun ServerInfoEntity.toDomainModel(): ServerInfo = ServerInfo(
    nbServiceCode = this.nbServiceCode ?: "",
    dbVersion = this.dbVersion ?: 0,
    meterManPwd = this.meterManPwd ?: "",
    serverName = this.serverName ?: "",
    serverSite = this.serverSite ?: "",
    asSiteId = this.asSiteId ?: 0,
    localGoverName = this.localGoverName ?: "",
    serverIP = this.serverIP ?: "",
    serverPort = this.serverPort ?: 0,
    serverURL = this.serverURL ?: "",
    serverConnectionCode = this.serverConnectionCode ?: 0,
    nbServerIp = this.nbServerIp ?: "",
    nbServerPort = this.nbServerPort ?: 0
)