package com.hitec.presentation.main.as_report

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.gson.Gson
import com.hitec.domain.model.AsDevice
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.theme.RenewSmartSetTheme

@Composable
fun AsReportScreen(
    navController: NavHostController,
    viewModel: AsReportViewModel = hiltViewModel()
) {
    InitScreen(navController = navController, viewModel = viewModel)

    val state by viewModel.container.stateFlow.collectAsState()
    val context = LocalContext.current

    AsReportScreen(state = state)
}

@Composable
private fun InitScreen(
    navController: NavHostController,
    viewModel: AsReportViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val asDeviceJson = navBackStackEntry?.arguments?.getString(ArgumentName.ARGU_AS_DEVICE)
    LaunchedEffect(asDeviceJson) {
        if (asDeviceJson != null) {
            val asDevice = Gson().fromJson(asDeviceJson, AsDevice::class.java)
            viewModel.asReportViewModelInit(asDevice)
        }
    }
}

@Composable
private fun AsReportScreen(
    state: AsReportState
) {
    Text(text = state.asDevice.reportNo.toString())
}

@Preview
@Composable
private fun AsReportScreenPreview() {
    RenewSmartSetTheme {
        AsReportScreen(state = AsReportState())
    }
}