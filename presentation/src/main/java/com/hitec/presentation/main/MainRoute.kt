package com.hitec.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainRoute(
    val route: String,
    val contentDescription: String,
    val icon: ImageVector
) {
    Main(route = "MainScreen", contentDescription = "메인", icon = Icons.Filled.List),
//    As(route = "AsScreen", contentDescription = "AS", icon = Icons.Filled.Build),
//    Camera(route = "CameraScreen", contentDescription = "카메라", icon = Icons.Filled.CameraAlt),
//    MyPage(route = "MyPageScreen", contentDescription = "내 정보", icon = Icons.Filled.AccountCircle),
//    Setting(route = "SettingScreen", contentDescription = "설정", icon = Icons.Filled.Settings),
}