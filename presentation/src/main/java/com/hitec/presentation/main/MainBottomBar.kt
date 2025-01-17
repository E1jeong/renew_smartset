package com.hitec.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hitec.presentation.navigation.MainNav
import com.hitec.presentation.navigation.NavigationUtils

@Composable
fun MainBottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    val mainBottomNavigationItems = listOf(
        MainNav.Camera,
        MainNav.InstallDevice,
        MainNav.AsDevice,
        MainNav.MyPage
    )

    Column {
        HorizontalDivider(thickness = 2.dp)
        NavigationBar(containerColor = Color.White) {
            mainBottomNavigationItems.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                    label = { item.label?.let { Text(it, textAlign = TextAlign.Center) } },
                    selected = currentRoute == item.route,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    onClick = {
                        NavigationUtils.navigate(
                            navController,
                            item.route,
                            navController.graph.startDestinationRoute
                        )
                    }
                )
            }
        }
    }
}