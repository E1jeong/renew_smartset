package com.hitec.presentation.util

import androidx.navigation.NavHostController
import com.hitec.presentation.main.Destination
import com.hitec.presentation.main.MainRoute
import com.hitec.presentation.main.RouteName
import com.hitec.presentation.main.SearchRoute

object NavigationUtils {
    fun navigate(
        controller: NavHostController,
        routeName: String,
        backStackRouteName: String? = null,
        isLaunchSingleTop: Boolean = true,
        needToRestoreState: Boolean = true
    ) {
        controller.navigate(routeName) {
            if (backStackRouteName != null) {
                popUpTo(backStackRouteName) {
                    saveState = true
                }
            }
            launchSingleTop = isLaunchSingleTop
            restoreState = needToRestoreState
        }
    }

    fun findDestination(route: String?): Destination {
        return when (route) {
            RouteName.CAMERA -> MainRoute.Camera
            RouteName.INSTALL_DEVICE -> MainRoute.InstallDevice
            RouteName.AS_DEVICE -> MainRoute.AsDevice
            RouteName.MY_PAGE -> MainRoute.MyPage
            RouteName.SEARCH -> SearchRoute
            else -> MainRoute.InstallDevice
        }
    }
}