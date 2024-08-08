package com.hitec.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.hitec.presentation.util.NavigationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHeader(
    viewModel: MainViewModel,
    navController: NavHostController,
    currentRoute: String?
) {
    TopAppBar(
        title = {
            Text(text = NavigationUtils.findDestination(currentRoute).title)
        },
        navigationIcon = {
            if (!MainRoute.isMainRoute(currentRoute)) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "BackIcon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        actions = {
            if (currentRoute == MainRoute.InstallDevice.route || currentRoute == MainRoute.AsDevice.route) {
                IconButton(
                    onClick = { viewModel.openSearchScreen(navController) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "SearchIcon"
                    )
                }
            }
        }
    )
}