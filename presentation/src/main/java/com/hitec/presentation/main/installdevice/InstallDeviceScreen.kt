package com.hitec.presentation.main.installdevice

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.main.MainSideEffect
import com.hitec.presentation.main.MainViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun InstallDeviceScreen(
    viewModel: MainViewModel
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainSideEffect.Toast -> {
                Toast.makeText(
                    context,
                    sideEffect.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    InstallDeviceScreen(state.installDeviceList)
}

@Composable
private fun InstallDeviceScreen(installDeviceList: List<InstallDevice>) {
    LazyColumn {
        items(installDeviceList.size) { index ->
            InstallDeviceCard(installDevice = installDeviceList[index])
        }
    }
}
