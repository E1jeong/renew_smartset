package com.hitec.presentation.main.device_menu

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.domain.usecase.main.as_report.GetAsDeviceFromImeiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DeviceMenuViewModel @Inject constructor(
    private val getAsDeviceFromImeiUseCase: GetAsDeviceFromImeiUseCase,
) : ViewModel(), ContainerHost<DeviceMenuState, DeviceMenuSideEffect> {
    override val container: Container<DeviceMenuState, DeviceMenuSideEffect> = container(
        initialState = DeviceMenuState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(DeviceMenuSideEffect.Toast(throwable.message.toString()))
                    Log.e(TAG, "error handler: ${throwable.message}")
                }
            }
        }
    )

    fun getDeviceImei(deviceImei: String) = intent {
        Log.d(TAG, "getDeviceImei: $deviceImei")
        reduce { state.copy(receivedImei = deviceImei) }
    }

    fun getAsDeviceFromImei(deviceImei: String) = intent {
        val result = getAsDeviceFromImeiUseCase(deviceImei).getOrNull()
        if (result != null) {
            reduce { state.copy(isExistAsDevice = true) }
        }
        Log.d(TAG, "getAsDeviceFromImei: $result")
    }

    fun onClickInstallButton() = intent {
        postSideEffect(DeviceMenuSideEffect.Toast("onClickInstallButton"))
    }

    fun onClickAsButton() = intent {
        postSideEffect(DeviceMenuSideEffect.Toast("onClickAsButton"))
    }

    fun navigateToDeviceDetail() = intent {
        postSideEffect(DeviceMenuSideEffect.NavigateToDeviceDetail(state.receivedImei))
    }

    fun navigateToAsReport() = intent {
        postSideEffect(DeviceMenuSideEffect.NavigateToAsReport(state.receivedImei))
    }

    fun navigateToPhotoUpload() = intent {
        postSideEffect(DeviceMenuSideEffect.NavigateToPhotoUpload(state.receivedImei))
    }

    companion object {
        const val TAG = "DeviceMenuViewModel"
    }
}

@Immutable
data class DeviceMenuState(
    val receivedImei: String = "",
    val isExistAsDevice: Boolean = false,
)

sealed interface DeviceMenuSideEffect {
    data class Toast(val message: String) : DeviceMenuSideEffect
    data class NavigateToDeviceDetail(val deviceImei: String) : DeviceMenuSideEffect
    data class NavigateToAsReport(val deviceImei: String) : DeviceMenuSideEffect
    data class NavigateToPhotoUpload(val deviceImei: String) : DeviceMenuSideEffect
}