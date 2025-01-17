package com.hitec.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.hitec.presentation.R
import com.hitec.presentation.navigation.MainNav
import com.hitec.presentation.navigation.NavigationUtils

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
            if (!MainNav.isMainRoute(currentRoute)) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
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
            if (currentRoute == MainNav.InstallDevice.route || currentRoute == MainNav.AsDevice.route) {
                IconButton(
                    onClick = { viewModel.openMapScreen(navController) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Map,
                        contentDescription = "map"
                    )
                }
                IconButton(
                    onClick = { viewModel.openSearchScreen(navController) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                }
            }
        }
    )
}