package com.hitec.data.db.retrofit

import com.hitec.data.model.DeleteInstallDBResponse
import com.hitec.data.model.DownloadInstallDbResponse
import com.hitec.data.model.LocalSiteListResponse
import com.hitec.data.model.SubAreaListResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface HitecService {
    companion object {
        const val SMART_SERVER_COMMON = "smart/meter/MeterW12Action.do?"
        const val METHOD = "method"
        const val USER_ID = "userId"
        const val PASSWORD = "password"
        const val MOBILE_ID = "mobileId"
        const val BLUETOOTH_ID = "bluetoothId"
        const val LOCAL_SITE = "localSite"
        const val AREA_CODE = "areaCd"
        const val REQUEST_AS_CODE = "reqAsCd"
        const val INSTALL_GROUP_CODE = "installGroupCd"
        const val COMPANY_CODE = "companyCd"
        const val CONNECTION_TYPE = "ConnectionType"
        const val FILE_NAME = "fileName"

        const val DOWNLOAD_LOCAL_INFO = "downloadLocalInfo"
        const val DOWNLOAD_SUB_AREA = "downloadSubArea"
        const val DOWNLOAD_INSTALL_DB = "downloadInstallDB"
        const val DELETE_INSTALL_DB = "deleteInstallDB"
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

    //설치한(+설치해야될) 단말기의 db url 다운로드
    @GET(SMART_SERVER_COMMON)
    suspend fun getInstallDbUrl(
        @Query(METHOD) method: String = DOWNLOAD_INSTALL_DB,
        @Query(USER_ID) userId: String,
        @Query(PASSWORD) password: String,
        @Query(MOBILE_ID) mobileId: String,
        @Query(BLUETOOTH_ID, encoded = true) bluetoothId: String,
        @Query(LOCAL_SITE) localSite: String,
        @Query(AREA_CODE) areaCd: String = "", //subArea에서 받아온 읍,면,동 지역코드
        @Query(REQUEST_AS_CODE) reqAsCd: String = "", // 0: 설치리스트, 1: 설치리스트 + AS리스트
        @Query(INSTALL_GROUP_CODE) installGroupCd: String = "", // null: 모든 데이터 다운로드, 1 ~ 10: 앱에서 설치시 사용한 그룹
        @Query(COMPANY_CODE) companyCd: String = "", // 0: 전체, 1: 하이텍
        @Query(CONNECTION_TYPE) connectionType: String = "", // 0: 일반, 1:VPN, 2:각 지역서버
    ): DownloadInstallDbResponse

    @Streaming
    @GET
    suspend fun getInstallDb(
        @Url url: String
    ): Response<ResponseBody>

    //downloadInstallDeviceListDB를 통해 받아온 URL(서버)에 생성된 DB 삭제
    @GET(SMART_SERVER_COMMON)
    suspend fun deleteInstallDb(
        @Query(METHOD) method: String = DELETE_INSTALL_DB,
        @Query(USER_ID) userId: String,
        @Query(PASSWORD) password: String,
        @Query(MOBILE_ID) mobileId: String,
        @Query(BLUETOOTH_ID, encoded = true) bluetoothId: String,
        @Query(FILE_NAME) fileName: String,
    ): DeleteInstallDBResponse
}