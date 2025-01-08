package com.hitec.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.SettingsCell
import androidx.compose.ui.graphics.vector.ImageVector
import com.hitec.presentation.navigation.ArgumentName.ARGU_AS_DEVICE
import com.hitec.presentation.navigation.ArgumentName.ARGU_INSTALL_DEVICE
import com.hitec.presentation.navigation.RouteName.AS_DEVICE
import com.hitec.presentation.navigation.RouteName.AS_REPORT
import com.hitec.presentation.navigation.RouteName.CAMERA
import com.hitec.presentation.navigation.RouteName.DEVICE_DETAIL
import com.hitec.presentation.navigation.RouteName.DEVICE_MENU
import com.hitec.presentation.navigation.RouteName.INSTALL_DEVICE
import com.hitec.presentation.navigation.RouteName.MAP
import com.hitec.presentation.navigation.RouteName.MY_PAGE
import com.hitec.presentation.navigation.RouteName.SEARCH

sealed class MainNav(
    override val route: String,
    override val title: String,
    val icon: ImageVector
) : Destination {

    data object Camera : MainNav(CAMERA, "Camera", Icons.Filled.CameraAlt)
    data object InstallDevice : MainNav(INSTALL_DEVICE, "Install Device", Icons.Filled.SettingsCell)
    data object AsDevice : MainNav(AS_DEVICE, "AS Device", Icons.Filled.Construction)
    data object MyPage : MainNav(MY_PAGE, "My page", Icons.Filled.AccountCircle)

    companion object {
        fun isMainRoute(route: String?): Boolean {
            return when (route) {
                CAMERA, INSTALL_DEVICE, AS_DEVICE, MY_PAGE -> true
                else -> false
            }
        }
    }
}

object SearchNav : Destination {
    override val route: String = SEARCH
    override val title: String = "Search"
}

object DeviceDetailNav : Destination {
    override val route: String = "$DEVICE_DETAIL/{$ARGU_INSTALL_DEVICE}"
    override val title: String = "DeviceDetail"
}

object AsReportNav : Destination {
    override val route: String = "$AS_REPORT/{$ARGU_AS_DEVICE}"
    override val title: String = "AsReport"
}

object MapNav : Destination {
    override val route: String = MAP
    override val title: String = "Map"
}

object DeviceMenuNav : Destination {
    override val route: String = DEVICE_MENU
    override val title: String = "DeviceMenu"
}

interface Destination {
    val route: String
    val title: String
}

object RouteName {
    const val CAMERA = "CameraScreen"
    const val INSTALL_DEVICE = "InstallDeviceScreen"
    const val AS_DEVICE = "AsDeviceScreen"
    const val MY_PAGE = "MyPageScreen"
    const val SEARCH = "SearchScreen"
    const val DEVICE_DETAIL = "DeviceDetailScreen"
    const val AS_REPORT = "AsReportScreen"
    const val MAP = "MapScreen"
    const val DEVICE_MENU = "DeviceMenuScreen"
}

object ArgumentName {
    const val ARGU_INSTALL_DEVICE = "installDevice"
    const val ARGU_AS_DEVICE = "asDevice"
}