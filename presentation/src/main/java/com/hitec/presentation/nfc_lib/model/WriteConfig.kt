package com.hitec.presentation.nfc_lib.model

data class WriteConfig(
    val serialNumber: String,
    val terminalProtocol: Int,
    val imsi: String, //ctn
    val serverIp: String,
    val serverPort: String,
    val firmwareVersion: String,
    val reportInterval: Int,
    val meterInterval: Int,
    val meterCount: Int,
    val meterInfo: List<MeterInfo>?,
)

data class MeterInfo(
    val meterPort: String,
    val meterType: String,
)
