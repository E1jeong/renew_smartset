package com.hitec.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

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