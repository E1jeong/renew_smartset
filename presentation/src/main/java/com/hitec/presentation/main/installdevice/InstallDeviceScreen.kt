package com.hitec.presentation.main.installdevice

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.R
import com.hitec.presentation.main.MainSideEffect
import com.hitec.presentation.main.MainViewModel
import com.hitec.presentation.main.component.InstallDeviceCard
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun InstallDeviceScreen(
    viewModel: MainViewModel
) {
    val state by viewModel.container.stateFlow.collectAsState()
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

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isNetworkLoading) {
            Text("로딩 중...")
        } else {
            InstallDeviceScreen(state.installDeviceList)
        }
    }
}

@Composable
private fun InstallDeviceScreen(installDeviceList: List<InstallDevice>) {
    LazyColumn {
        items(installDeviceList.size) { index ->
            InstallDeviceCard(installDevice = installDeviceList[index])
        }
    }
}
