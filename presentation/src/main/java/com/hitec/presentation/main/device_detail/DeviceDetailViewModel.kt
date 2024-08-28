package com.hitec.presentation.main.device_detail

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.usecase.LoginScreenInfoUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadableImageListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    private val loginScreenInfoUseCase: LoginScreenInfoUseCase,
    private val postDownloadableImageListUseCase: PostDownloadableImageListUseCase
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
        getLoginScreenInfo()

        viewModelScope.launch {
            container.stateFlow.collect { state ->
                state.installDevice?.let {
                    getDownloadableImageList()
                }
            }
        }
    }

    @OptIn(OrbitExperimental::class)
    private fun getLoginScreenInfo() = blockingIntent {
        val loginScreenInfo = loginScreenInfoUseCase.getLoginScreenInfo().getOrThrow()

        reduce {
            state.copy(
                id = loginScreenInfo.id,
                password = loginScreenInfo.password,
                localSite = loginScreenInfo.localSite,
                androidDeviceId = loginScreenInfo.androidDeviceId,
                isNetworkLoading = true,
            )
        }
    }

    private fun getDownloadableImageList() = intent {
        val response = postDownloadableImageListUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSite,
            meterDeviceId = state.installDevice?.meterDeviceId ?: "",
            deviceTypeCd = state.installDevice?.deviceTypeCd ?: "",
        ).getOrDefault(emptyList())

        reduce {
            state.copy(
                downloadableImageList = response.map { it.photoTypeCd }.toSet()
            )
        }
    }

    fun setInstallDevice(installDevice: InstallDevice) = intent {
        reduce {
            state.copy(installDevice = installDevice)
        }
    }

    companion object {
        const val TAG = "DeviceDetailViewModel"
    }
}

@Immutable
data class DeviceDetailState(
    val installDevice: InstallDevice? = null,
    val id: String = "",
    val password: String = "",
    val localSite: String = "",
    val androidDeviceId: String = "",
    val isNetworkLoading: Boolean = false,
    val downloadableImageList: Set<Int> = emptySet(),
)

sealed interface DeviceDetailSideEffect {
    class Toast(val message: String) : DeviceDetailSideEffect
}