package com.hitec.data.db.retrofit.param

data class UploadAsEssentialParam(
    val cdmaNo: String,
    val nwk: String,
    val firmware: String,
    val serverName: String,
    val serverSite: String,
    val fieldActionMain: String,
    val fieldActionSub: String,
    val consumeHouseNo: String,
    val deviceSn: String,
) {
    fun toBody(): String =
        "cdmaNo^nwk^firmware^serverName^serverSite^fieldActionMain^fieldActionSub^consumeHouseNo^deviceSn|" +
                "$cdmaNo^$nwk^$firmware^$serverName^$serverSite^$fieldActionMain^$fieldActionSub^$consumeHouseNo^$deviceSn"
}