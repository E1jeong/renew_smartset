package com.hitec.presentation.main.device_detail

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.model.ServerInfo
import com.hitec.domain.model.UploadInstallDevice
import com.hitec.domain.usecase.login.LoginScreenInfoUseCase
import com.hitec.domain.usecase.main.GetServerInfoUseCase
import com.hitec.domain.usecase.main.device_detail.GetInstallDeviceFromImeiUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadDeviceImageUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadableImageListUseCase
import com.hitec.domain.usecase.main.device_detail.PostUploadInstallDeviceUseCase
import com.hitec.domain.usecase.main.device_detail.PostUploadInstallEssentialUseCase
import com.hitec.domain.usecase.main.device_detail.UpdateInstallDeviceUseCase
import com.hitec.presentation.R
import com.hitec.presentation.main.as_report.AsReportViewModel
import com.hitec.presentation.nfc_lib.NfcManager
import com.hitec.presentation.nfc_lib.NfcRequest
import com.hitec.presentation.nfc_lib.NfcResponse
import com.hitec.presentation.nfc_lib.NfcResponse.Companion.BOARD_ACK_FLAG_ACTIVE
import com.hitec.presentation.nfc_lib.NfcResponse.Companion.BOARD_ACK_FLAG_RESET
import com.hitec.presentation.nfc_lib.NfcResponse.Companion.BOARD_ACK_FLAG_SLEEP
import com.hitec.presentation.nfc_lib.NfcResponse.Companion.SERVER_COMM_FLAG_CHECK
import com.hitec.presentation.nfc_lib.NfcResponse.Companion.SERVER_COMM_FLAG_REQUEST
import com.hitec.presentation.util.PathHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val nfcManager: NfcManager,
    private val nfcRequest: NfcRequest,
    private val nfcResponse: NfcResponse,
    private val loginScreenInfoUseCase: LoginScreenInfoUseCase,
    private val postDownloadableImageListUseCase: PostDownloadableImageListUseCase,
    private val postDownloadDeviceImageUseCase: PostDownloadDeviceImageUseCase,
    private val updateInstallDeviceUseCase: UpdateInstallDeviceUseCase,
    private val postUploadInstallEssentialUseCase: PostUploadInstallEssentialUseCase,
    private val postUploadInstallDeviceUseCase: PostUploadInstallDeviceUseCase,
    private val getServerInfoUseCase: GetServerInfoUseCase,
    private val getInstallDeviceFromImeiUseCase: GetInstallDeviceFromImeiUseCase,
) : ViewModel(), ContainerHost<DeviceDetailState, DeviceDetailSideEffect> {

    override val container: Container<DeviceDetailState, DeviceDetailSideEffect> = container(
        initialState = DeviceDetailState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(DeviceDetailSideEffect.Toast(throwable.message.toString()))
                    Log.e(TAG, "error handler: ${throwable.message}")
                }
            }
        }
    )

    init {
        collectResultFlow()
        collectWriteConfigFlow()
        getServerInfo()
    }

    private fun getServerInfo() = intent {
        val serverInfo = getServerInfoUseCase().getOrThrow()
        reduce { state.copy(serverInfo = serverInfo) }
        Log.e(AsReportViewModel.TAG, "getServerInfo: $serverInfo")
    }

    private fun collectResultFlow() {
        viewModelScope.launch {
            nfcResponse.nfcResultFlow.collect { nfcResultFlow ->
                Log.d(TAG, "nfcResponse.nfcResultFlow: $nfcResultFlow")

                intent {
                    reduce { state.copy(nfcResult = nfcResultFlow) }
                }
            }
        }
    }

    private fun collectWriteConfigFlow() {
        viewModelScope.launch {
            nfcResponse.nfcWriteConfigFlow.collect { writeConfigFlow ->
                intent {
                    reduce {
                        state.copy(
                            installDevice = state.installDevice.copy(
                                meterDeviceSn = writeConfigFlow.serialNumber,
                                cdmaNo = writeConfigFlow.imsi,
                                serverAddr1 = writeConfigFlow.serverIp,
                                serverPort1 = writeConfigFlow.serverPort,
                                firmware = writeConfigFlow.firmwareVersion,
                                reportIntervalTime = writeConfigFlow.reportInterval.toString(),
                                meterIntervalTime = writeConfigFlow.meterInterval.toString(),
                                meterCount = writeConfigFlow.meterCount.toString()
                            )
                        )
                    }

                    updateInstallDeviceUseCase(state.installDevice)
                }
            }
        }
    }

    //From DeviceDetailScreen navController, init InstallDevice
    fun initialize(deviceImei: String) = intent {
        getInstallDeviceFromImeiUseCase(deviceImei).getOrNull()?.let {
            reduce { state.copy(installDevice = it) }
        }

        getLoginScreenInfo()
        getDownloadableImageList()

        if (state.downloadableImageList.isNotEmpty()) {
            setImageSaveDir(context = context)

            for (photoTypeCd in state.downloadableImageList) {
                getDeviceImages(photoTypeCd)
            }
        }

        selectedDevice = state.installDevice
    }

    private fun setImageSaveDir(context: Context) = intent {
        val appName = context.getString(R.string.app_name)
        val photoDirName = context.getString(R.string.directory_photo)
        val path = "$appName/$photoDirName/${state.localSite}/${state.installDevice.consumeHouseNo}"
        PathHelper.deleteDir(path) // delete old image files
        PathHelper.isExistDir(path)

        reduce { state.copy(imagePath = path) }
    }

    private fun getLoginScreenInfo() = blockingIntent {
        val loginScreenInfo = loginScreenInfoUseCase.getLoginScreenInfo().getOrThrow()

        reduce {
            state.copy(
                id = loginScreenInfo.id,
                password = loginScreenInfo.password,
                localSite = loginScreenInfo.localSiteEngWrittenByUser, // if loginScreenInfo.localSite == Korean, error
                androidDeviceId = loginScreenInfo.androidDeviceId
            )
        }
    }

    private fun getDownloadableImageList() = blockingIntent {
        val response = postDownloadableImageListUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSite,
            meterDeviceId = state.installDevice.meterDeviceId,
            deviceTypeCd = state.installDevice.deviceTypeCd ?: "",
        ).getOrDefault(emptyList())

        val resultCode = response.first().resultCd
        if (resultCode != -1) {
            reduce {
                state.copy(
                    downloadableImageList = response.map { it.photoTypeCd }.toSet().toSortedSet()
                )
            }
        }
    }

    private fun getDeviceImages(photoTypeCd: Int) = intent {
        val response = postDownloadDeviceImageUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSite,
            meterDeviceId = state.installDevice.meterDeviceId,
            deviceTypeCd = state.installDevice.deviceTypeCd ?: "",
            meterCd = "", // check empty string value (not null), because always get a response
            photoTypeCd = photoTypeCd
        ).getOrThrow()

        val image = ImageManager.saveBase64ToImage(
            context = context,
            base64Str = response.imageData,
            fileName = response.imageName,
            imagePath = state.imagePath
        )

        reduce { state.copy(deviceImageList = state.deviceImageList + Pair(photoTypeCd, image)) }
    }

    fun setTerminalProtocolInWriteConfig(terminalProtocol: String) = intent {
        reduce { state.copy(terminalProtocolInWriteConfig = terminalProtocol) }
    }

    fun setIpPortInWriteConfig(ipPort: String) = intent {
        var resultIpPort = ""
        when (ipPort) {
            "LG Business" -> {
                resultIpPort = "106.103.250.108:5783"
            }

            "LG Dev" -> {
                resultIpPort = "106.103.233.155:5783"
            }

            "KT Business" -> {
                resultIpPort = "192.168.151.84:9189"
            }

            "KT Dev" -> {
                resultIpPort = "112.175.172.106:9189"
            }
        }

        reduce { state.copy(ipPortInWriteConfig = resultIpPort) }
    }

    fun setMeterIntervalInWriteConfig(meterInterval: String) = intent {
        reduce { state.copy(meterIntervalInWriteConfig = meterInterval) }
    }

    fun setReportIntervalInWriteConfig(reportInterval: String) = intent {
        reduce { state.copy(reportIntervalInWriteConfig = reportInterval) }
    }

    fun onTextChangeInUpdateFirmware(firmware: String) = blockingIntent {
        reduce { state.copy(userInputFirmwareInUpdateFirmware = firmware.trim()) }
    }

    fun onClearUserInputFirmwareInUpdateFirmware() = intent {
        reduce { state.copy(userInputFirmwareInUpdateFirmware = "") }
    }

    fun onTextChangeInChangeRiHourToMinute(userInput: String) = blockingIntent {
        reduce { state.copy(userInputMinuteInChangeRiHourToMinute = userInput.trim()) }
    }

    fun onClearUserInputMinuteInChangeRiHourToMinute() = intent {
        reduce { state.copy(userInputMinuteInChangeRiHourToMinute = "") }
    }

    fun nfcRequestChangeSerial() = intent {
        nfcManager.start()
        nfcRequest.changeSerial(
            serialNumber = state.nfcRequestChangeSerialUserInput,
            length = state.nfcRequestChangeSerialUserInput.length
        )
    }

    fun clearNfcResult() = intent {
        reduce { state.copy(nfcResult = "Tag nfc") }
    }

    fun onTextChangeInChangeSerialDialog(userInput: String) = blockingIntent {
        reduce { state.copy(nfcRequestChangeSerialUserInput = userInput.trim()) }
    }

    fun clearNfcRequestChangeSerialUserInput() = intent {
        reduce { state.copy(nfcRequestChangeSerialUserInput = "") }
    }

    fun nfcRequestReadConfig() {
        nfcManager.start()
        nfcRequest.nodeConfig()
    }

    fun nfcRequestSetSleep() {
        NfcResponse.boardControlAckFlag = BOARD_ACK_FLAG_SLEEP
        nfcManager.start()
        nfcRequest.setSleepOrActive(1) // 1: sleep
    }

    fun nfcRequestSetActive() {
        NfcResponse.boardControlAckFlag = BOARD_ACK_FLAG_ACTIVE
        nfcManager.start()
        nfcRequest.setSleepOrActive(2) // 2: active
    }

    fun nfcRequestResetDevice() {
        NfcResponse.boardControlAckFlag = BOARD_ACK_FLAG_RESET
        nfcManager.start()
        nfcRequest.resetDevice()
    }

    fun nfcRequestReadMeter() {
        nfcManager.start()
        nfcRequest.readMeter()
    }

    fun nfcRequestWriteConfig() = intent {
        nfcManager.start()
        nfcRequest.setNbConfig(
            consumeHouseNo = state.installDevice.consumeHouseNo ?: "",
            serialNo = state.installDevice.meterDeviceSn,
            amiMeteringInterval = Integer.parseInt(state.meterIntervalInWriteConfig),
            amiReportInterval = Integer.parseInt(state.reportIntervalInWriteConfig),
            terminalProtocol = if (state.terminalProtocolInWriteConfig == "1.6") 163 else 164,
            strServiceCode = "HSVR",
            strServerIp = state.ipPortInWriteConfig.substringBefore(":"),
            strServerPort = state.ipPortInWriteConfig.substringAfter(":"),
            meterNum = 1,
            meterType0 = 1,
            meterPort0 = 1,
            meterType1 = 0,
            meterPort1 = 0,
            meterType2 = 0,
            meterPort2 = 0
        )
    }

    fun nfcRequestReqComm() {
        NfcResponse.serverCommFlag = SERVER_COMM_FLAG_REQUEST
        nfcManager.start()
        nfcRequest.reqServerConnect(1)
    }

    fun nfcRequestCheckComm() {
        NfcResponse.serverCommFlag = SERVER_COMM_FLAG_CHECK
        nfcManager.start()
        nfcRequest.reqServerConnect(0)
    }

    fun nfcRequestUpdateFirmwareBsl() = intent {
        //reqMode: 0 -> NB BSL ready
        //reqMode: 1 -> NB BSL start => use in NfcResponse.updateFirmware()

        // use in NfcResponse.updateFirmware() when reqMode is 0
        userInputFirmware = state.userInputFirmwareInUpdateFirmware

        nfcManager.start()
        nfcRequest.reqFwUpdate(
            serialNo = state.installDevice.meterDeviceSn,
            reqMode = 0,
            fwVersion = state.userInputFirmwareInUpdateFirmware.ifEmpty { state.installDevice.firmware }
        )

        reduce { state.copy(userInputFirmwareInUpdateFirmware = "") } // init value
    }

    fun nfcRequestUpdateFirmwareFota() = intent {
        //reqMode: 2 -> NB, GSM FOTA

        nfcManager.start()
        nfcRequest.reqFwUpdate(
            serialNo = state.installDevice.meterDeviceSn,
            reqMode = 2,
            fwVersion = state.installDevice.firmware,
        )
    }

    fun nfcRequestChangeRiHourToMinute() = intent {
        nfcManager.start()
        nfcRequest.changeMinuteInterval(value = Integer.parseInt(state.userInputMinuteInChangeRiHourToMinute))
    }

    fun nfcRequestReadPeriodData() = intent {
        nfcManager.start()
        nfcRequest.reqPeriodMeterData(
            1,
            state.startDate.toString(),
            state.endDate.toString()
        )
        nfcRequest.reqFlashData(
            state.startDate.toString(),
            state.endDate.toString()
        )
    }

    fun setStartDate(date: LocalDate) = intent {
        reduce { state.copy(startDate = date) }
    }

    fun setEndDate(date: LocalDate) = intent {
        reduce { state.copy(endDate = date) }
    }

    private suspend fun postUploadInstallEssential(state: DeviceDetailState): Int {
        val response = postUploadInstallEssentialUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSite,

            cdmaNo = state.installDevice.cdmaNo ?: "",
            nwk = state.installDevice.nwk ?: "",
            firmware = state.installDevice.firmware ?: "",
            serverName = state.serverInfo.serverName,
            serverSite = state.serverInfo.serverSite,
            consumeHouseNo = state.installDevice.consumeHouseNo ?: "",
            deviceSn = state.installDevice.meterDeviceSn ?: "",
        ).getOrThrow()

        return response.msgCode
    }

    private suspend fun postUploadInstallDevice(state: DeviceDetailState): UploadInstallDevice {
        return postUploadInstallDeviceUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSite,

            modTypeCd = state.installDevice.modTypeCd ?: "",
            meterDeviceId = state.installDevice.meterDeviceId,
            deviceTypeCd = state.installDevice.deviceTypeCd ?: "",
            communicationTypeCd = state.installDevice.communicationTypeCd ?: "",
            telecomTypeCd = state.installDevice.telecomTypeCd ?: "",
            consumeHouseNo = state.installDevice.consumeHouseNo ?: "",
            consumeHouseNm = state.installDevice.consumeHouseNm ?: "",
            meterMethodCd = state.installDevice.meterMethodCd ?: "",
            deviceModelCd = state.installDevice.deviceModelCd ?: "",
            meterDeviceNm = state.installDevice.meterDeviceNm ?: "",
            meterDeviceSn = state.installDevice.meterDeviceSn ?: "",
            installGroupCd = state.installDevice.installGroupCd ?: "",
            companyCd = state.installDevice.companyCd ?: "",
            installCompanyNm = "", //TODO ?
            nbServiceCode = state.installDevice.nbServiceCode ?: "",
            nbCseId = state.installDevice.nbCseId ?: "",
            nbIccId = state.installDevice.nbIccId ?: "",
            uploadResultCd = state.installDevice.uploadResultCd ?: "",
            uploadErrorCd = state.installDevice.uploadErrorCd ?: "",
            barcode = state.installDevice.barcode ?: "",
            cameraSave = state.installDevice.cameraSave ?: "",
            setDt = state.installDevice.setDt ?: "",
            AreaBig = state.installDevice.AreaBig ?: "",
            AreaBigCd = state.installDevice.AreaBigCd ?: "",
            AreaMid = state.installDevice.AreaMid ?: "",
            AreaMidCd = state.installDevice.AreaMidCd ?: "",
            AreaSmall = state.installDevice.AreaSmall ?: "",
            AreaSmallCd = state.installDevice.AreaSmallCd ?: "",
            setAreaAddr = state.installDevice.setAreaAddr ?: "",
            setPlaceDesc = state.installDevice.setPlaceDesc ?: "",
            accountCheckNote = state.installDevice.accountCheckNote ?: "",
            deviceMemo = state.installDevice.deviceMemo ?: "",
            cdmaNo = state.installDevice.cdmaNo ?: "",
            serverAddr1 = state.installDevice.serverAddr1 ?: "",
            serverPort1 = state.installDevice.serverPort1 ?: "",
            pan = state.installDevice.pan ?: "",
            panNm = state.installDevice.panNm ?: "",
            pnwk = state.installDevice.pnwk ?: "",
            nwk = state.installDevice.nwk ?: "",
            mac = state.installDevice.mac ?: "",
            gpsLatitude = state.installDevice.gpsLatitude ?: "",
            gpsLongitude = state.installDevice.gpsLongitude ?: "",
            meterBaseTime = state.installDevice.meterBaseTime ?: "",
            meterIntervalTime = state.installDevice.meterIntervalTime ?: "",
            reportIntervalTime = state.installDevice.reportIntervalTime ?: "",
            reportRangeTime = state.installDevice.reportRangeTime ?: "",
            dataSkipFlag = state.installDevice.dataSkipFlag ?: "",
            meterStoreMonth = state.installDevice.meterStoreMonth ?: "",
            meterBaseDay = state.installDevice.meterBaseDay ?: "",
            meterAlertTime = state.installDevice.meterAlertTime ?: "",
            meterPeriodTime = state.installDevice.meterPeriodTime ?: "",
            activeStartDay = state.installDevice.activeStartDay ?: "",
            activeEndDay = state.installDevice.activeEndDay ?: "",
            activeStartHour = state.installDevice.activeStartHour ?: "",
            activeEndHour = state.installDevice.activeEndHour ?: "",
            terminalType = state.installDevice.terminalType ?: "",
            firmware = state.installDevice.firmware ?: "",
            subUpdateInterval = state.installDevice.subUpdateInterval ?: "",
            subNwkID = state.installDevice.subNwkID ?: "",
            terminalTypeCd = state.installDevice.terminalTypeCd ?: "",
            utilityCode = state.installDevice.utilityCode ?: "",
            waterCompCode = state.installDevice.waterCompCode ?: "",
            wmuManufacturerCode = state.installDevice.wmuManufacturerCode ?: "",
            wmuInstallCode = state.installDevice.wmuInstallCode ?: "",
            cdmaTypeCd = state.installDevice.cdmaTypeCd ?: "",
            firmwareGateway = state.installDevice.firmwareGateway ?: "",
            serverConnectionCode = state.installDevice.serverConnectionCode ?: "",
            concenIP = state.installDevice.concenIP ?: "",
            concenGwIP = state.installDevice.concenGwIP ?: "",
            serverURL = state.installDevice.serverURL ?: "",
            rfChn = state.installDevice.rfChn ?: "",
            hcuId = state.installDevice.hcuId ?: "",
            setInitDate = state.installDevice.setInitDate ?: "",
            masterSn = state.installDevice.masterSn ?: "",
            subSn1 = state.installDevice.subSn1 ?: "",
            subSn2 = state.installDevice.subSn2 ?: "",
            subSn3 = state.installDevice.subSn3 ?: "",
            subSn4 = state.installDevice.subSn4 ?: "",
            accountMeterUse1 = state.installDevice.accountMeterUse1 ?: "",
            accountMeterUse2 = state.installDevice.accountMeterUse2 ?: "",
            accountMeterUse3 = state.installDevice.accountMeterUse3 ?: "",
            meterCount = state.installDevice.meterCount ?: "",
            meterCd1 = state.installDevice.meterCd1 ?: "",
            meterPort1 = state.installDevice.meterPort1 ?: "",
            meterTypeCd1 = state.installDevice.meterTypeCd1 ?: "",
            meterCompany1 = state.installDevice.meterCompany1 ?: "",
            meterSn1 = state.installDevice.meterSn1 ?: "",
            meterCurrVal1 = state.installDevice.meterCurrVal1 ?: "",
            meterCaliber1 = state.installDevice.meterCaliber1 ?: "",
            metercaliberCd1 = state.installDevice.metercaliberCd1 ?: "",
            meterDigits1 = state.installDevice.meterDigits1 ?: "",
            meterCd2 = state.installDevice.meterCd2 ?: "",
            meterPort2 = state.installDevice.meterPort2 ?: "",
            meterTypeCd2 = state.installDevice.meterTypeCd2 ?: "",
            meterCompany2 = state.installDevice.meterCompany2 ?: "",
            meterSn2 = state.installDevice.meterSn2 ?: "",
            meterCurrVal2 = state.installDevice.meterCurrVal2 ?: "",
            meterCaliber2 = state.installDevice.meterCaliber2 ?: "",
            metercaliberCd2 = state.installDevice.metercaliberCd2 ?: "",
            meterDigits2 = state.installDevice.meterDigits2 ?: "",
            meterCd3 = state.installDevice.meterCd3 ?: "",
            meterPort3 = state.installDevice.meterPort3 ?: "",
            meterTypeCd3 = state.installDevice.meterTypeCd3 ?: "",
            meterCompany3 = state.installDevice.meterCompany3 ?: "",
            meterSn3 = state.installDevice.meterSn3 ?: "",
            meterCurrVal3 = state.installDevice.meterCurrVal3 ?: "",
            meterCaliber3 = state.installDevice.meterCaliber3 ?: "",
            metercaliberCd3 = state.installDevice.metercaliberCd3 ?: "",
            meterDigits3 = state.installDevice.meterDigits3 ?: "",
        ).getOrThrow()
    }

    fun uploadInstallDevice() = intent {
        if (postUploadInstallEssential(state) == -1) {
            return@intent
        }
        Log.e(TAG, "uploadInstallDevice -> postUploadInstallEssential")

        val response = postUploadInstallDevice(state)
        Log.e(TAG, "uploadInstallDevice: $response")

        reduce {
            state.copy(
                isUploadResultDialogVisible = true,
                uploadResult = "upload install device success",
                installDevice = state.installDevice.copy(
                    modTypeCd = "U",
                    uploadResultCd = response.resultCd,
                    uploadErrorCd = response.errorCd,
                )
            )
        }

        updateInstallDeviceUseCase(state.installDevice).getOrThrow()
    }

    fun onUploadResultDialogDismiss() = intent {
        reduce { state.copy(isUploadResultDialogVisible = false) }
    }

    companion object {
        private const val TAG = "DeviceDetailViewModel"

        var userInputFirmware = ""
        var selectedDevice = InstallDevice(meterDeviceId = "HT-T-012345")
    }
}

@Immutable
data class DeviceDetailState(
    val installDevice: InstallDevice = InstallDevice(meterDeviceId = "HT-T-012345"), // this is init value, don't mind
    val id: String = "",
    val password: String = "",
    val localSite: String = "",
    val androidDeviceId: String = "",
    val downloadableImageList: Set<Int> = emptySet(),
    val deviceImageList: List<Pair<Int, Any?>> = emptyList(),
    val imagePath: String = "",
    val nfcResult: String = "Tag nfc",
    val nfcRequestChangeSerialUserInput: String = "",
    val terminalProtocolInWriteConfig: String = "1.6",
    val ipPortInWriteConfig: String = "LG Business",
    val meterIntervalInWriteConfig: String = "1",
    val reportIntervalInWriteConfig: String = "6",
    val userInputFirmwareInUpdateFirmware: String = "",
    val userInputMinuteInChangeRiHourToMinute: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = LocalDate.now(),
    val serverInfo: ServerInfo = ServerInfo(
        nbServiceCode = "",
        dbVersion = 0,
        meterManPwd = "",
        serverName = "",
        serverSite = "",
        asSiteId = 0,
        localGoverName = "",
        serverIP = "",
        serverPort = 0,
        serverURL = "",
        serverConnectionCode = 0,
        nbServerIp = "",
        nbServerPort = 0
    ),
    val isUploadResultDialogVisible: Boolean = false,
    val uploadResult: String = "",
)

sealed interface DeviceDetailSideEffect {
    class Toast(val message: String) : DeviceDetailSideEffect
}