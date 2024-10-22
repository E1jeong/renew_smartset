package com.hitec.presentation.main.device_detail

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.usecase.LoginScreenInfoUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadDeviceImageUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadableImageListUseCase
import com.hitec.domain.usecase.main.device_detail.UpdateInstallDeviceUseCase
import com.hitec.presentation.R
import com.hitec.presentation.nfc_lib.NfcManager
import com.hitec.presentation.nfc_lib.NfcRequest
import com.hitec.presentation.nfc_lib.NfcResponse
import com.hitec.presentation.nfc_lib.NfcResponse.Companion.BOARD_ACK_FLAG_ACTIVE
import com.hitec.presentation.nfc_lib.NfcResponse.Companion.BOARD_ACK_FLAG_RESET
import com.hitec.presentation.nfc_lib.NfcResponse.Companion.BOARD_ACK_FLAG_SLEEP
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
    fun initialize(installDevice: InstallDevice) = intent {
        reduce { state.copy(installDevice = installDevice) }

        getLoginScreenInfo()
        getDownloadableImageList()

        if (state.downloadableImageList.isNotEmpty()) {
            setImageSaveDir(context = context)

            for (photoTypeCd in state.downloadableImageList) {
                getDeviceImages(photoTypeCd)
            }
        }
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

    companion object {
        private const val TAG = "DeviceDetailViewModel"
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
)

sealed interface DeviceDetailSideEffect {
    class Toast(val message: String) : DeviceDetailSideEffect
}