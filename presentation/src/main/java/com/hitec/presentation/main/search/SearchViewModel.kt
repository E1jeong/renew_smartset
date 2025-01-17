package com.hitec.presentation.main.search

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.hitec.domain.model.AsDevice
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.usecase.main.search.GetAsDeviceListFromImeiAndSubAreaUseCase
import com.hitec.domain.usecase.main.search.GetAsDeviceListFromSubAreaUseCase
import com.hitec.domain.usecase.main.search.GetInstallDeviceListFromImeiAndSubAreaUseCase
import com.hitec.domain.usecase.main.search.GetInstallDeviceListFromSubAreaUseCase
import com.hitec.presentation.main.MainViewModel
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.navigation.AsReportNav
import com.hitec.presentation.navigation.DeviceDetailNav
import com.hitec.presentation.navigation.NavigationUtils
import com.hitec.presentation.navigation.RouteName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getInstallDeviceListFromSubAreaUseCase: GetInstallDeviceListFromSubAreaUseCase,
    private val getInstallDeviceListFromImeiAndSubAreaUseCase: GetInstallDeviceListFromImeiAndSubAreaUseCase,
    private val getAsDeviceListFromSubAreaUseCase: GetAsDeviceListFromSubAreaUseCase,
    private val getAsDeviceListFromImeiAndSubAreaUseCase: GetAsDeviceListFromImeiAndSubAreaUseCase,
) : ViewModel(), ContainerHost<SearchState, SearchSideEffect> {

    override val container: Container<SearchState, SearchSideEffect> = container(
        initialState = SearchState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(SearchSideEffect.Toast(throwable.message.toString()))
                    Log.e(TAG, "error handler: ${throwable.message}")
                }
            }
        }
    )

    init {
        getSubAreaListFromMainViewModel()
    }

    fun setPreviousRoute(route: String) = intent {
        reduce { state.copy(previousRoute = route) }
    }

    fun openDeviceDetailScreen(navHostController: NavHostController, installDevice: InstallDevice) {
        val route =
            DeviceDetailNav.route.replace("{${ArgumentName.ARGU_INSTALL_DEVICE}}", installDevice.nwk ?: "")

        NavigationUtils.navigate(
            controller = navHostController,
            routeName = route,
        )
    }

    fun openAsReportScreen(navHostController: NavHostController, asDevice: AsDevice) {
        val route =
            AsReportNav.route.replace("{${ArgumentName.ARGU_AS_DEVICE}}", asDevice.nwk ?: "")

        NavigationUtils.navigate(
            controller = navHostController,
            routeName = route,
            backStackRouteName = RouteName.SEARCH
        )
    }

    private fun getSubAreaListFromMainViewModel() = intent {
        MainViewModel.subAreaListState.collectLatest { subAreaList ->
            reduce { state.copy(subAreaList = subAreaList) }
            Log.i(TAG, "getSubAreaListFromMainViewModel: $subAreaList")
        }
    }

    fun onChipSelected(selectedChip: String) = intent {
        reduce {
            if (state.selectedChip == selectedChip) { // 이미 선택된 칩을 다시 클릭하면 선택 해제
                state.copy(selectedChip = "", searchedInstallDeviceList = emptyList())
            } else { // 새로운 칩 선택
                state.copy(selectedChip = selectedChip)
            }
        }

        if (state.selectedChip.isNotEmpty()) {
            when (state.previousRoute) {
                RouteName.INSTALL_DEVICE -> searchInstallDeviceFromSubArea()
                RouteName.AS_DEVICE -> searchAsDeviceFromSubArea()
            }
        }
    }

    fun search(query: String) = intent {

        postSideEffect(SearchSideEffect.Toast("Searching for: $query"))

        reduce { state.copy(isNetworkLoading = true) }

        when (state.previousRoute) {
            RouteName.INSTALL_DEVICE -> {
                val searchedInstallDeviceList =
                    getInstallDeviceListFromImeiAndSubAreaUseCase(
                        subArea = state.selectedChip,
                        imei = query
                    ).getOrThrow()

                reduce { state.copy(searchedInstallDeviceList = searchedInstallDeviceList) }
            }

            RouteName.AS_DEVICE -> {
                val searchedAsDeviceList =
                    getAsDeviceListFromImeiAndSubAreaUseCase(
                        subArea = state.selectedChip,
                        imei = query
                    ).getOrThrow()

                reduce { state.copy(searchedAsDeviceList = searchedAsDeviceList) }
            }
        }

        reduce { state.copy(isNetworkLoading = false) }
    }

    private fun searchInstallDeviceFromSubArea() = intent {
        reduce { state.copy(isNetworkLoading = true) }
        val searchedInstallDeviceList =
            getInstallDeviceListFromSubAreaUseCase(state.selectedChip).getOrThrow()

        reduce {
            state.copy(
                searchedInstallDeviceList = searchedInstallDeviceList,
                isNetworkLoading = false
            )
        }
    }

    private fun searchAsDeviceFromSubArea() = intent {
        reduce { state.copy(isNetworkLoading = true) }
        val searchedAsDeviceList =
            getAsDeviceListFromSubAreaUseCase(state.selectedChip).getOrThrow()

        reduce {
            state.copy(
                searchedAsDeviceList = searchedAsDeviceList,
                isNetworkLoading = false
            )
        }
    }

    companion object {
        const val TAG = "SearchViewModel"
    }
}

@Immutable
data class SearchState(
    val subAreaList: List<String> = emptyList(),
    val selectedChip: String = "",
    val searchQuery: String = "",
    val searchedInstallDeviceList: List<InstallDevice> = emptyList(),
    val searchedAsDeviceList: List<AsDevice> = emptyList(),
    val isNetworkLoading: Boolean = false,
    val previousRoute: String = "",
)

sealed interface SearchSideEffect {
    class Toast(val message: String) : SearchSideEffect
}