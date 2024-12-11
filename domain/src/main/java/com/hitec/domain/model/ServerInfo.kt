package com.hitec.domain.model

data class ServerInfo(
    val nbServiceCode: String,
    val dbVersion: Int,
    val meterManPwd: String,
    val serverName: String,
    val serverSite: String,
    val asSiteId: Int,
    val localGoverName: String,
    val serverIP: String,
    val serverPort: Int,
    val serverURL: String,
    val serverConnectionCode: Int,
    val nbServerIp: String,
    val nbServerPort: Int
)
