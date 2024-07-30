package com.hitec.data.db.retrofit

import com.hitec.data.model.LocalSiteListResponse
import com.hitec.data.model.SubAreaListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HitecService {
    companion object {
        const val SMART_SERVER_COMMON = "smart/meter/MeterW12Action.do?"
        const val METHOD = "method"
        const val USER_ID = "userId"
        const val PASSWORD = "password"
        const val MOBILE_ID = "mobileId"
        const val BLUETOOTH_ID = "bluetoothId"
        const val LOCAL_SITE = "localSite"

        const val DOWNLOAD_LOCAL_INFO = "downloadLocalInfo"
        const val DOWNLOAD_SUB_AREA = "downloadSubArea"
    }

    @GET(SMART_SERVER_COMMON)
    suspend fun getLocalSite(
        @Query(METHOD) method: String = DOWNLOAD_LOCAL_INFO,
        @Query(USER_ID) userId: String,
        @Query(PASSWORD) password: String,
        @Query(MOBILE_ID) mobileId: String,
        //encoded 옵션은 콜론(:)을 string 값에 그대로 적용 하기 위해 사용 (true)
        @Query(BLUETOOTH_ID, encoded = true) bluetoothId: String,
    ): LocalSiteListResponse

    //해당 지역의 읍,면,동 이름 리스트 다운로드
    @GET(SMART_SERVER_COMMON)
    suspend fun getSubArea(
        @Query(METHOD) method: String = DOWNLOAD_SUB_AREA,
        @Query(USER_ID) userId: String,
        @Query(PASSWORD) password: String,
        @Query(MOBILE_ID) mobileId: String,
        @Query(BLUETOOTH_ID, encoded = true) bluetoothId: String,
        @Query(LOCAL_SITE) localSite: String,
    ): SubAreaListResponse

//    //설치한(+설치해야될) 단말기의 db url 다운로드
//    @GET(SMART_SERVER_COMMON)
//    fun downloadInstallDeviceListDB(
//        @Query(METHOD) method: String = "downloadInstallDB",
//        @Query(USER_ID) userId: String = "won9964",
//        @Query(PASSWORD) password: String = "01",
//        @Query(MOBILE_ID) mobileId: String = "won9964",
//        @Query(BLUETOOTH_ID, encoded = true) bluetoothId: String = "65:2D:84:1A:C9:38:88:09",
//        @Query("localSite") localSite: String,
//        @Query("areaCd") areaCd: String = "", //subArea에서 받아온 읍,면,동 지역코드
//        @Query("reqAsCd") reqAsCd: String = "", // 0: 설치리스트, 1: 설치리스트 + AS리스트
//        @Query("installGroupCd") installGroupCd: String = "", // null: 모든 데이터 다운로드, 1 ~ 10: 앱에서 설치시 사용한 그룹
//        @Query("companyCd") companyCd: String = "", // 0: 전체, 1: 하이텍
//        @Query("ConnectionType") connectionType: String = "", // 0: 일반, 1:VPN, 2:각 지역서버
//    ): Call<DbUrlData>
//
//    @Streaming
//    @GET
//    fun getDBFromUrl(
//        @Url url: String
//    ): Call<ResponseBody>
//
//    //downloadInstallDeviceListDB를 통해 받아온 URL(서버)에 생성된 DB 삭제
//    @GET(SMART_SERVER_COMMON)
//    fun deleteDBFile(
//        @Query(METHOD) method: String = "deleteInstallDB",
//        @Query(USER_ID) userId: String = "won9964",
//        @Query(PASSWORD) password: String = "01",
//        @Query(MOBILE_ID) mobileId: String = "won9964",
//        @Query(BLUETOOTH_ID, encoded = true) bluetoothId: String = "65:2D:84:1A:C9:38:88:09",
//        @Query("fileName") fileName: String,
//    ): Call<ResponseBody>
}