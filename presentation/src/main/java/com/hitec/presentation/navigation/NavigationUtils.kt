package com.hitec.presentation.navigation

import androidx.navigation.NavHostController

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
            RouteName.CAMERA -> MainNav.Camera
            RouteName.INSTALL_DEVICE -> MainNav.InstallDevice
            RouteName.AS_DEVICE -> MainNav.AsDevice
            RouteName.MY_PAGE -> MainNav.MyPage
            RouteName.SEARCH -> SearchNav
            else -> MainNav.InstallDevice
        }
    }
}