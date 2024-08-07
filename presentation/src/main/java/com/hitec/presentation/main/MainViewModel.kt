package com.hitec.presentation.main

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.domain.usecase.GetInstallDbUrlUseCase
import com.hitec.domain.usecase.GetInstallDbUseCase
import com.hitec.domain.usecase.GetSubAreaUseCase
import com.hitec.domain.usecase.LoginScreenInfoUseCase
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
class MainViewModel @Inject constructor(
    private val getSubAreaUseCase: GetSubAreaUseCase,
    private val loginScreenInfoUseCase: LoginScreenInfoUseCase,
    private val getInstallDbUrlUseCase: GetInstallDbUrlUseCase,
    private val getInstallDbUseCase: GetInstallDbUseCase,
) : ViewModel(), ContainerHost<MainState, MainSideEffect> {

    override val container: Container<MainState, MainSideEffect> = container(
        initialState = MainState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(MainSideEffect.Toast(throwable.message.toString()))
                    Log.e(TAG, "error handler: ${throwable.message}")
                }
            }
        }
    )

    init {
        getLoginScreenInfo()
        getSubArea()
        getInstallDevice()
    }

    private fun getSubArea() = intent {
        getSubAreaUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSiteEngWrittenByUser
        ).getOrThrow()
    }

    private fun getLoginScreenInfo() = blockingIntent {
        val loginScreenInfo = loginScreenInfoUseCase.getLoginScreenInfo().getOrThrow()

        reduce {
            state.copy(
                id = loginScreenInfo.id,
                password = loginScreenInfo.password,
                localSite = loginScreenInfo.localSite,
                androidDeviceId = loginScreenInfo.androidDeviceId,
                localSiteEngWrittenByUser = loginScreenInfo.localSiteEngWrittenByUser
            )
        }
    }

    private fun getInstallDevice() = intent {
        val url = getInstallDbUrlUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSiteEngWrittenByUser
        ).getOrThrow()

        getInstallDbUseCase(url).getOrThrow()
    }

    fun onQrCodeValueChange(qrCodeValue: String) = intent {
        reduce { state.copy(qrCodeValue = qrCodeValue) }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}

@Immutable
data class MainState(
    val id: String = "",
    val password: String = "",
    val localSite: String = "",
    val androidDeviceId: String = "",
    val localSiteEngWrittenByUser: String = "",
    val qrCodeValue: String = "No QR Code detected",
)

sealed interface MainSideEffect {
    class Toast(val message: String) : MainSideEffect
}