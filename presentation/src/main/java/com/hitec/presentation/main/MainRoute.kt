package com.hitec.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainRoute(
    val route: String,
    val contentDescription: String,
    val icon: ImageVector
) {
    CAMERA(route = "CameraScreen", contentDescription = "카메라", icon = Icons.Filled.CameraAlt),
    MAIN(route = "MainScreen", contentDescription = "메인", icon = Icons.Filled.List),
    AS_DEVICE(route = "AsDeviceScreen", contentDescription = "AS", icon = Icons.Filled.Recycling),
    MY_PAGE(route = "MyPageScreen", contentDescription = "내 정보", icon = Icons.Filled.AccountCircle),
}