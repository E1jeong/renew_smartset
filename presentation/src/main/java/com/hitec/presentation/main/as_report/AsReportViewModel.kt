package com.hitec.presentation.main.as_report

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.domain.model.AsCode
import com.hitec.domain.model.AsDevice
import com.hitec.domain.model.LoginScreenInfo
import com.hitec.domain.model.ServerInfo
import com.hitec.domain.model.UploadAsDevice
import com.hitec.domain.usecase.login.LoginScreenInfoUseCase
import com.hitec.domain.usecase.main.GetServerInfoUseCase
import com.hitec.domain.usecase.main.as_report.GetAsCodeUseCase
import com.hitec.domain.usecase.main.as_report.PostUploadAsDeviceUseCase
import com.hitec.domain.usecase.main.as_report.PostUploadAsEssentialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
class AsReportViewModel @Inject constructor(
    private val getAsCodeUseCase: GetAsCodeUseCase,
    private val loginScreenInfoUseCase: LoginScreenInfoUseCase,
    private val postUploadAsEssentialUseCase: PostUploadAsEssentialUseCase,
    private val postUploadAsDeviceUseCase: PostUploadAsDeviceUseCase,
    private val getServerInfoUseCase: GetServerInfoUseCase,
) : ViewModel(), ContainerHost<AsReportState, AsReportSideEffect> {

    companion object {
        const val TAG = "AsReportViewModel"
    }

    override val container: Container<AsReportState, AsReportSideEffect> = container(
        initialState = AsReportState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(AsReportSideEffect.Toast(throwable.message.toString()))
                    Log.e(TAG, "error handler: ${throwable.message}")
                }
            }
        }
    )

    init {
        getLoginScreenInfo()
        getAsCodeContent()
        getAsCodeContentDetail()
        getServerInfo()
    }

    fun asReportViewModelInit(asDevice: AsDevice) = intent {
        reduce { state.copy(asDevice = asDevice) }
    }

    private fun getAsCodeContent() = intent {
        val asCodeContent = getAsCodeUseCase("CC34").getOrThrow()
        reduce { state.copy(asCodeContent = asCodeContent) }
        Log.e(TAG, "getAsCodeContent: $asCodeContent")
    }

    private fun getAsCodeContentDetail() = intent {
        val asCodeContentDetail = getAsCodeUseCase("CC35").getOrThrow()
        reduce { state.copy(asCodeContentDetail = asCodeContentDetail) }
        Log.e(TAG, "getAsCodeContentDetail: $asCodeContentDetail")
    }

    private fun getLoginScreenInfo() = blockingIntent {
        val loginScreenInfo = loginScreenInfoUseCase.getLoginScreenInfo().getOrThrow()
        reduce { state.copy(loginScreenInfo = loginScreenInfo) }
        Log.e(TAG, "getLoginScreenInfo: $loginScreenInfo")
    }

    private fun getServerInfo() = intent {
        val serverInfo = getServerInfoUseCase().getOrThrow()
        reduce { state.copy(serverInfo = serverInfo) }
        Log.e(TAG, "getServerInfo: $serverInfo")
    }

    fun onAsRequestCommentChange(comment: String) = blockingIntent {
        reduce { state.copy(asRequestComment = comment.trim()) }
    }

    fun onAsHandlingMemoChange(memo: String) = blockingIntent {
        reduce { state.copy(asHandlingMemo = memo.trim()) }
    }

    fun onHandlingContentChange(content: String) = intent {
        reduce { state.copy(handlingContent = content) }
    }

    fun onHandlingContentDetailChange(contentDetail: String) = intent {
        reduce { state.copy(handlingContentDetail = contentDetail) }
    }

    fun onUploadResultDialogDismiss() = intent {
        reduce { state.copy(isUploadResultDialogVisible = false) }
    }

    private suspend fun uploadAsEssential(state: AsReportState): Int {
        val uploadAsEssentialResponse = postUploadAsEssentialUseCase(
            userId = state.loginScreenInfo.id,
            password = state.loginScreenInfo.password,
            mobileId = state.loginScreenInfo.id,
            bluetoothId = state.loginScreenInfo.androidDeviceId,
            localSite = state.loginScreenInfo.localSite,
            cdmaNo = state.asDevice.cdmaNo ?: "",
            nwk = state.asDevice.nwk ?: "",
            firmware = state.asDevice.firmware ?: "",
            serverName = state.serverInfo.serverName,
            serverSite = state.serverInfo.serverSite,
            fieldActionMain = state.asCodeContent.find { it.asCodeName == state.handlingContent }?.asCodeId.toString(),
            fieldActionSub = state.asCodeContentDetail.find { it.asCodeName == state.handlingContentDetail }?.asCodeId.toString(),
            consumeHouseNo = state.asDevice.consumeHouseNo ?: "",
            deviceSn = state.asDevice.deviceSn ?: "",
        ).getOrThrow()

        return uploadAsEssentialResponse.msgCode
    }

    private suspend fun uploadAsDevice(state: AsReportState): UploadAsDevice {
        return postUploadAsDeviceUseCase(
            //url
            userId = state.loginScreenInfo.id,
            password = state.loginScreenInfo.password,
            mobileId = state.loginScreenInfo.id,
            bluetoothId = state.loginScreenInfo.androidDeviceId,
            localSite = state.loginScreenInfo.localSite,

            //param
            modTypeCd = "U",
            reportNo = state.asDevice.reportNo ?: "",
            siteId = state.asDevice.siteId ?: "",
            receiptDt = LocalDate.now().toString(),
            receiptUserId = state.loginScreenInfo.id,
            uploadResultCd = state.asDevice.uploadResultCd ?: "",
            uploadErrorCd = state.asDevice.uploadErrorCd ?: "",
            receiptType = "1",
            receiptMemo = state.asRequestComment,
            consumeHouseNo = state.asDevice.consumeHouseNo ?: "",
            firstSetDt = state.asDevice.firstSetDt ?: "",
            meterMethodCd = state.asDevice.meterMethodCd ?: "",
            deviceSn = state.asDevice.deviceSn ?: "",
            pan = state.asDevice.pan ?: "",
            nwk = state.asDevice.nwk ?: "",
            terminalTypeCd = state.asDevice.terminalTypeCd ?: "",
            meterTypeCd = state.asDevice.meterTypeCd ?: "",
            deviceTypeCd = state.asDevice.deviceTypeCd ?: "",
            communicationTypeCd = state.asDevice.communicationTypeCd ?: "",
            telecomTypeCd = state.asDevice.telecomTypeCd ?: "",
            productYear = state.asDevice.productYear ?: "",
            deviceModelCd = state.asDevice.deviceModelCd ?: "", //device model
            caliberCd = state.asDevice.caliberCd ?: "",
            fieldActionMain = state.asCodeContent.find { it.asCodeName == state.handlingContent }?.asCodeId.toString(),
            fieldActionSub = state.asCodeContentDetail.find { it.asCodeName == state.handlingContentDetail }?.asCodeId.toString(),
            fieldActionMemo = "${state.handlingContent}/${state.handlingContentDetail}/${state.asHandlingMemo}",
            analysisType = "",
            analysisTypeDetail = "",
            statusSet = "1",
            perNext = state.asDevice.perNext ?: "",
            installCompanyNm = "하이텍",
            firmware = state.asDevice.firmware ?: "",
        ).getOrThrow()
    }

    fun uploadAsReport() = intent {
        if (uploadAsEssential(state) == -1) {
            return@intent
        }
        Log.e(TAG, "uploadAsReport -> uploadAsEssential")

        val uploadAsDeviceResponse = uploadAsDevice(state)
        Log.e(TAG, "test: $uploadAsDeviceResponse")

        reduce {
            state.copy(
                isUploadResultDialogVisible = true,
                uploadResult = "upload success",
                asDevice = state.asDevice.copy(
                    modTypeCd = "U",
                    reportNo = uploadAsDeviceResponse.serverReportNo,
                    uploadResultCd = uploadAsDeviceResponse.resultCd,
                    uploadErrorCd = uploadAsDeviceResponse.errorCd,
                    perNext = uploadAsDeviceResponse.perNext,
                    statusSet = uploadAsDeviceResponse.statusSet
                )
            )
        }
    }
}

@Immutable
data class AsReportState(
    val asDevice: AsDevice = AsDevice(meterDeviceId = "HT-T-012345"), // this is init value, don't mind
    val asCodeContent: List<AsCode> = emptyList(),
    val asCodeContentDetail: List<AsCode> = emptyList(),
    val asRequestComment: String = "",
    val asHandlingMemo: String = "",
    val handlingContent: String = "",
    val handlingContentDetail: String = "",
    val loginScreenInfo: LoginScreenInfo = LoginScreenInfo(
        id = "",
        password = "",
        localSite = "",
        androidDeviceId = "",
        isSwitchOn = false,
        localSiteEngWrittenByUser = ""
    ),
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

sealed interface AsReportSideEffect {
    class Toast(val message: String) : AsReportSideEffect
}