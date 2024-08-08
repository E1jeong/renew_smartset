package com.hitec.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import com.hitec.presentation.main.RouteName.AS_DEVICE
import com.hitec.presentation.main.RouteName.CAMERA
import com.hitec.presentation.main.RouteName.DEEP_LINK_SCHEME
import com.hitec.presentation.main.RouteName.INSTALL_DEVICE
import com.hitec.presentation.main.RouteName.MY_PAGE
import com.hitec.presentation.main.RouteName.SEARCH

sealed class MainRoute(
    override val route: String,
    override val title: String,
    val icon: ImageVector
) : Destination {
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$route" }
    )

    data object Camera : MainRoute(CAMERA, "Camera", Icons.Filled.CameraAlt)
    data object InstallDevice : MainRoute(INSTALL_DEVICE, "Install Device", Icons.Filled.List)
    data object AsDevice : MainRoute(AS_DEVICE, "AS Device", Icons.Filled.Recycling)
    data object MyPage : MainRoute(MY_PAGE, "My page", Icons.Filled.AccountCircle)

    companion object {
        fun isMainRoute(route: String?): Boolean {
            return when (route) {
                CAMERA, INSTALL_DEVICE, AS_DEVICE, MY_PAGE -> true
                else -> false
            }
        }
    }
}

object SearchRoute : Destination {
    override val route: String = SEARCH
    override val title: String = "Search"
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$route" }
    )
}

interface Destination {
    val route: String
    val title: String
    val deepLinks: List<NavDeepLink>
}

object RouteName {
    const val DEEP_LINK_SCHEME = "SmartSet://"
    const val CAMERA = "CameraScreen"
    const val INSTALL_DEVICE = "InstallDeviceScreen"
    const val AS_DEVICE = "AsDeviceScreen"
    const val MY_PAGE = "MyPageScreen"
    const val SEARCH = "SearchScreen"
}