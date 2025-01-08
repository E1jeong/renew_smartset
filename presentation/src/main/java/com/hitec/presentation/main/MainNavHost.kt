package com.hitec.presentation.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hitec.presentation.main.as_report.AsReportScreen
import com.hitec.presentation.main.asdevice.AsDeviceScreen
import com.hitec.presentation.main.camera.CameraScreen
import com.hitec.presentation.main.device_detail.DeviceDetailScreen
import com.hitec.presentation.main.device_menu.DeviceMenuScreen
import com.hitec.presentation.main.installdevice.InstallDeviceScreen
import com.hitec.presentation.main.map.MapScreen
import com.hitec.presentation.main.mypage.MyPageScreen
import com.hitec.presentation.main.search.SearchScreen
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.navigation.AsReportNav
import com.hitec.presentation.navigation.DeviceDetailNav
import com.hitec.presentation.navigation.DeviceMenuNav
import com.hitec.presentation.navigation.MainNav
import com.hitec.presentation.navigation.MapNav
import com.hitec.presentation.navigation.SearchNav

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainNavHost(sharedViewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentRoute = navBackStackEntry?.destination?.route

    Surface {
        Scaffold(
            topBar = {
                MainHeader(
                    viewModel = sharedViewModel,
                    navController = navController,
                    currentRoute = currentRoute
                )
            },
            content = { paddingValues ->
                NavHost(
                    modifier = Modifier.padding(paddingValues),
                    navController = navController,
                    startDestination = MainNav.InstallDevice.route
                ) {
                    composable(route = MainNav.InstallDevice.route) {
                        InstallDeviceScreen(
                            navController = navController,
                            viewModel = sharedViewModel
                        )
                    }
                    composable(route = MainNav.Camera.route) {
                        CameraScreen(sharedViewModel)
                    }
                    composable(route = MainNav.AsDevice.route) {
                        AsDeviceScreen(
                            navController = navController,
                            viewModel = sharedViewModel
                        )
                    }
                    composable(route = MainNav.MyPage.route) {
                        MyPageScreen()
                    }
                    composable(route = SearchNav.route) {
                        SearchScreen(navController = navController)
                    }
                    composable(
                        route = DeviceDetailNav.route,
                        arguments = listOf(
                            navArgument(ArgumentName.ARGU_INSTALL_DEVICE) {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        DeviceDetailScreen(navController = navController)
                    }
                    composable(
                        route = AsReportNav.route,
                    ) {
                        AsReportScreen(navController = navController)
                    }
                    composable(route = MapNav.route) {
                        MapScreen(
                            navController = navController,
                            viewModel = sharedViewModel
                        )
                    }
                    composable(route = DeviceMenuNav.route) {
                        DeviceMenuScreen(navController = navController)
                    }
                }
            },
            bottomBar = {
                if (MainNav.isMainRoute(currentRoute)) {
                    MainBottomBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        )
    }
}