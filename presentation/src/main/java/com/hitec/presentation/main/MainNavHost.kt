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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hitec.presentation.main.asdevice.AsDeviceScreen
import com.hitec.presentation.main.camera.CameraScreen
import com.hitec.presentation.main.installdevice.InstallDeviceScreen
import com.hitec.presentation.main.mypage.MyPageScreen
import com.hitec.presentation.main.search.SearchScreen

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
                    startDestination = MainRoute.InstallDevice.route
                ) {
                    composable(
                        route = MainRoute.InstallDevice.route,
                        deepLinks = MainRoute.InstallDevice.deepLinks
                    ) {
                        InstallDeviceScreen(sharedViewModel)
                    }
                    composable(
                        route = MainRoute.Camera.route,
                        deepLinks = MainRoute.Camera.deepLinks
                    ) {
                        CameraScreen(sharedViewModel)
                    }
                    composable(
                        route = MainRoute.AsDevice.route,
                        deepLinks = MainRoute.AsDevice.deepLinks
                    ) {
                        AsDeviceScreen()
                    }
                    composable(
                        route = MainRoute.MyPage.route,
                        deepLinks = MainRoute.MyPage.deepLinks
                    ) {
                        MyPageScreen()
                    }
                    composable(
                        route = SearchRoute.route,
                        deepLinks = SearchRoute.deepLinks
                    ) {
                        SearchScreen()
                    }
                }
            },
            bottomBar = {
                if (MainRoute.isMainRoute(currentRoute)) {
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