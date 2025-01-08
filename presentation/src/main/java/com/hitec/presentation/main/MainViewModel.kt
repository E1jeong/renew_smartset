package com.hitec.presentation.main

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.hitec.domain.model.AsDevice
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.usecase.login.LoginScreenInfoUseCase
import com.hitec.domain.usecase.main.DeleteInstallDbUseCase
import com.hitec.domain.usecase.main.GetAsDeviceUseCase
import com.hitec.domain.usecase.main.GetInstallDbUrlUseCase
import com.hitec.domain.usecase.main.GetInstallDbUseCase
import com.hitec.domain.usecase.main.GetInstallDeviceUseCase
import com.hitec.domain.usecase.main.GetSubAreaUseCase
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.navigation.AsReportNav
import com.hitec.presentation.navigation.DeviceMenuNav
import com.hitec.presentation.navigation.MapNav
import com.hitec.presentation.navigation.NavigationUtils
import com.hitec.presentation.navigation.RouteName
import com.hitec.presentation.navigation.SearchNav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val getInstallDeviceUseCase: GetInstallDeviceUseCase,
    private val deleteInstallDbUseCase: DeleteInstallDbUseCase,
    private val getAsDeviceUseCase: GetAsDeviceUseCase,
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

    private fun getLoginScreenInfo() = blockingIntent {
        val loginScreenInfo = loginScreenInfoUseCase.getLoginScreenInfo().getOrThrow()

        reduce {
            state.copy(
                id = loginScreenInfo.id,
                password = loginScreenInfo.password,
                localSite = loginScreenInfo.localSite,
                androidDeviceId = loginScreenInfo.androidDeviceId,
                localSiteEngWrittenByUser = loginScreenInfo.localSiteEngWrittenByUser,
                isNetworkLoading = true,
            )
        }
    }

    private fun getSubArea() = intent {
        val subAreaResponse = getSubAreaUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSiteEngWrittenByUser
        ).getOrThrow()

        val subAreaList = subAreaResponse.subAreaInfo.map { it.areaName }
        reduce { state.copy(subAreaList = subAreaList) }
        subAreaListState.value = subAreaList // to pass data SearchScreen
    }

    fun onQrCodeValueChange(qrCodeValue: String) = intent {
        reduce { state.copy(qrCodeValue = qrCodeValue) }
    }

    fun openSearchScreen(navHostController: NavHostController) {
        NavigationUtils.navigate(navHostController, SearchNav.route)
    }

    fun openMapScreen(navHostController: NavHostController) {
        NavigationUtils.navigate(navHostController, MapNav.route)
    }

    fun openDeviceMenuScreen(navHostController: NavHostController, installDevice: InstallDevice) {
        val route = DeviceMenuNav.route.replace("{${ArgumentName.ARGU_DEVICE_IMEI}}", installDevice.nwk ?: "")
        NavigationUtils.navigate(
            controller = navHostController,
            routeName = route,
        )
    }

    fun openAsReportScreen(navHostController: NavHostController, asDevice: AsDevice) {
        val gson = Gson()
        val asDeviceJson = gson.toJson(asDevice)
        val encodedJson = Uri.encode(asDeviceJson)
        val route =
            AsReportNav.route.replace("{${ArgumentName.ARGU_AS_DEVICE}}", encodedJson)

        NavigationUtils.navigate(
            controller = navHostController,
            routeName = route,
            backStackRouteName = RouteName.AS_DEVICE
        )
    }

    private fun getInstallDevice() = intent {
        //get sqlite db url
        val url = getInstallDbUrlUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSiteEngWrittenByUser
        ).getOrThrow()

        //get sqlite db to use url
        getInstallDbUseCase(url).getOrThrow()

        //get install device list from sqlite db to room api
        val installDeviceList = getInstallDeviceUseCase().getOrThrow()

        //extract db file name from url
        val dbFileName = url.substringAfterLast("/")

        //delete sqlite db file in server
        deleteInstallDbUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            fileName = dbFileName
        ).getOrThrow()

        //as device db depend on install device db
        getAsDevice()

        reduce {
            state.copy(
                installDeviceList = installDeviceList,
                isNetworkLoading = false,
            )
        }
    }

    private fun getAsDevice() = intent {
        val asDeviceList = getAsDeviceUseCase().getOrThrow()

        reduce {
            state.copy(
                asDeviceList = asDeviceList
            )
        }
    }

    companion object {
        const val TAG = "MainViewModel"

        //MainViewModel -> SearchViewModel
        val subAreaListState = MutableStateFlow<List<String>>(emptyList())
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
    val installDeviceList: List<InstallDevice> = emptyList(),
    val subAreaList: List<String> = emptyList(),
    val isNetworkLoading: Boolean = false,
    val asDeviceList: List<AsDevice> = emptyList(),
)

sealed interface MainSideEffect {
    class Toast(val message: String) : MainSideEffect
}