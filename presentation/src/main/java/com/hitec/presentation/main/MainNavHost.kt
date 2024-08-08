package com.hitec.presentation.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hitec.presentation.R
import com.hitec.presentation.main.asdevice.AsDeviceScreen
import com.hitec.presentation.main.camera.CameraScreen
import com.hitec.presentation.main.installdevice.InstallDeviceScreen
import com.hitec.presentation.main.mypage.MyPageScreen

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavHost(sharedViewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    }
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
                }
            },
            bottomBar = {
                if (MainRoute.isMainRoute(currentRoute)) {
                    MainBottomBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            }
        )
    }
}