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
        return when {
            route == null -> MainNav.InstallDevice
            route.contains(RouteName.CAMERA) -> MainNav.Camera
            route.contains(RouteName.INSTALL_DEVICE) -> MainNav.InstallDevice
            route.contains(RouteName.AS_DEVICE) -> MainNav.AsDevice
            route.contains(RouteName.MY_PAGE) -> MainNav.MyPage
            route.contains(RouteName.SEARCH) -> SearchNav
            route.contains(RouteName.DEVICE_DETAIL) -> DeviceDetailNav
            route.contains(RouteName.AS_REPORT) -> AsReportNav
            else -> MainNav.InstallDevice
        }
    }
}