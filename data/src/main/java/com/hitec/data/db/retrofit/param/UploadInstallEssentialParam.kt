package com.hitec.data.db.retrofit.param

data class UploadInstallEssentialParam(
    val cdmaNo: String,
    val nwk: String,
    val firmware: String,
    val serverName: String,
    val serverSite: String,
    val consumeHouseNo: String,
    val deviceSn: String,
) {
    fun toBody(): String =
        "cdmaNo^nwk^firmware^serverName^serverSite^consumeHouseNo^deviceSn|" +
                "$cdmaNo^$nwk^$firmware^$serverName^$serverSite^$consumeHouseNo^$deviceSn"
}
