package com.hitec.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hitec.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavHost(mainViewModel: MainViewModel) {
    val navController = rememberNavController()

    Surface {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    }
                )
            },
            content = { paddingValues ->
                NavHost(
                    modifier = Modifier.padding(paddingValues),
                    navController = navController,
                    startDestination = MainRoute.Main.route
                ) {
                    composable(MainRoute.Main.route) { MainScreen(mainViewModel) }
                }
            },
            bottomBar = { MainBottomBar(navController = navController) }
        )
    }
}