package com.hitec.presentation.main.device_detail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.theme.RenewSmartSetTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun DeviceDetailScreen(
    installDevice: InstallDevice,
    viewModel: DeviceDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val context = LocalContext.current

    Log.d("DeviceDetailScreen", "DeviceDetailScreen installDevice: $installDevice")

    // installDevice를 ViewModel로 전달
    LaunchedEffect(installDevice) {
        viewModel.setInstallDevice(installDevice)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is DeviceDetailSideEffect.Toast -> {
                Toast.makeText(
                    context,
                    sideEffect.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    DeviceDetailScreen(state.installDevice)
}

@Composable
private fun DeviceDetailScreen(installDevice: InstallDevice?) {
    Column {
        Text(text = "DeviceDetailScreen")
        Text(text = "${installDevice?.consumeHouseNm}")
    }
}


@Preview
@Composable
fun DeviceDetailScreenPreview() {
    RenewSmartSetTheme {
        //DeviceDetailScreen()
    }
}