package com.hitec.presentation.main.device_menu

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.navigation.DeviceDetailNav
import com.hitec.presentation.navigation.NavigationUtils
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
class DeviceMenuViewModel @Inject constructor() : ViewModel(), ContainerHost<DeviceMenuState, DeviceMenuSideEffect> {
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

    init {

    }

    fun getDeviceImei(deviceImei: String) = intent {
        Log.d(TAG, "getDeviceImei: $deviceImei")
        reduce { state.copy(receivedImei = deviceImei) }
    }

    fun onClickInstallButton() = intent {
        postSideEffect(DeviceMenuSideEffect.Toast("onClickInstallButton"))
    }

    fun onClickAsButton() = intent {
        postSideEffect(DeviceMenuSideEffect.Toast("onClickAsButton"))
    }

    fun openDeviceDetailScreen(navHostController: NavHostController, deviceImei: String) {
        val route = DeviceDetailNav.route.replace("{${ArgumentName.ARGU_INSTALL_DEVICE}}", deviceImei)

        NavigationUtils.navigate(
            controller = navHostController,
            routeName = route,
        )
    }

    companion object {
        const val TAG = "DeviceMenuViewModel"
    }
}

@Immutable
data class DeviceMenuState(
    val receivedImei: String = "",
)

sealed interface DeviceMenuSideEffect {
    class Toast(val message: String) : DeviceMenuSideEffect
}