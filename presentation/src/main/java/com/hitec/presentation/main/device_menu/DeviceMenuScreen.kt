package com.hitec.presentation.main.device_menu

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.theme.RenewSmartSetTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun DeviceMenuScreen(
    navController: NavHostController,
    viewModel: DeviceMenuViewModel = hiltViewModel()
) {
    InitScreen(navController = navController, viewModel = viewModel)

    DeviceMenuScreen(
        onClickInstallButton = viewModel::onClickInstallButton,
        onClickAsButton = viewModel::onClickAsButton,
        onClickDeviceDetailButton = viewModel::onClickDeviceDetailButton
    )
}

@Composable
private fun InitScreen(
    navController: NavHostController,
    viewModel: DeviceMenuViewModel
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val asDeviceJson = navBackStackEntry?.arguments?.getString(ArgumentName.ARGU_AS_DEVICE)
//    LaunchedEffect(asDeviceJson) {
//        if (asDeviceJson != null) {
//            val asDevice = Gson().fromJson(asDeviceJson, AsDevice::class.java)
//            viewModel.asReportViewModelInit(asDevice)
//        }
//    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is DeviceMenuSideEffect.Toast -> {
                Toast.makeText(
                    context,
                    sideEffect.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
private fun DeviceMenuScreen(
    onClickInstallButton: () -> Unit,
    onClickAsButton: () -> Unit,
    onClickDeviceDetailButton: () -> Unit
) {
    val buttons = listOf(
        "Install" to onClickInstallButton,
        "As" to onClickAsButton,
        "Device detail" to onClickDeviceDetailButton,
        "Device detail" to onClickDeviceDetailButton,
        "Device detail" to onClickDeviceDetailButton,
        "Device detail" to onClickDeviceDetailButton,
        "Device detail" to onClickDeviceDetailButton,
        "Device detail" to onClickDeviceDetailButton,
        "Device detail" to onClickDeviceDetailButton,
        "Device detail" to onClickDeviceDetailButton,
        "Device detail" to onClickDeviceDetailButton,
        "Device detail" to onClickDeviceDetailButton,
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 열의 그리드
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // 외부 여백
        horizontalArrangement = Arrangement.spacedBy(16.dp), // 버튼 간의 가로 간격
        verticalArrangement = Arrangement.spacedBy(16.dp) // 버튼 간의 세로 간격
    ) {
        items(buttons.size) { index ->
            val (buttonName, action) = buttons[index]
            Button(
                onClick = action,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Text(text = buttonName)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DeviceDetailScreenPreview() {
    RenewSmartSetTheme {
        Surface {
            DeviceMenuScreen(
                onClickInstallButton = {},
                onClickAsButton = {},
                onClickDeviceDetailButton = {}
            )
        }
    }
}