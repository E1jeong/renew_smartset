package com.hitec.presentation.main.as_report

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.domain.model.AsCode
import com.hitec.domain.model.AsDevice
import com.hitec.domain.model.LoginScreenInfo
import com.hitec.domain.usecase.login.LoginScreenInfoUseCase
import com.hitec.domain.usecase.main.as_report.GetAsCodeUseCase
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
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class AsReportViewModel @Inject constructor(
    private val getAsCodeUseCase: GetAsCodeUseCase,
    private val loginScreenInfoUseCase: LoginScreenInfoUseCase,
    private val postUploadAsEssentialUseCase: PostUploadAsEssentialUseCase,
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
        Log.e(TAG, "getLoginScreenInfo: $loginScreenInfo")

        reduce { state.copy(loginScreenInfo = loginScreenInfo) }
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

    fun uploadAsEssential() = intent {
        val uploadAsEssential = postUploadAsEssentialUseCase(
            userId = state.loginScreenInfo.id,
            password = state.loginScreenInfo.password,
            mobileId = state.loginScreenInfo.id,
            bluetoothId = state.loginScreenInfo.androidDeviceId,
            localSite = state.loginScreenInfo.localSite,
            cdmaNo = state.asDevice.cdmaNo ?: "",
            nwk = state.asDevice.nwk ?: "",
            firmware = state.asDevice.firmware ?: "",
            serverName = "개발",
            serverSite = state.loginScreenInfo.localSiteEngWrittenByUser,
            fieldActionMain = state.asCodeContent.find { it.asCodeName == state.handlingContent }?.asCodeId.toString(),
            fieldActionSub = state.asCodeContentDetail.find { it.asCodeName == state.handlingContentDetail }?.asCodeId.toString(),
            consumeHouseNo = state.asDevice.consumeHouseNo ?: "",
            deviceSn = state.asDevice.deviceSn ?: "",
        ).getOrThrow()
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
    )
)

sealed interface AsReportSideEffect {
    class Toast(val message: String) : AsReportSideEffect
}