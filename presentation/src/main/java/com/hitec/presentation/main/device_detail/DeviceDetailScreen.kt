package com.hitec.presentation.main.device_detail

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.gson.Gson
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.main.device_detail.component.NfcExtendedFab
import com.hitec.presentation.main.device_detail.component.NfcMenu
import com.hitec.presentation.main.device_detail.dialog.NfcResultDialog
import com.hitec.presentation.main.device_detail.dialog.nfc_request.NfcRequestChangeSerialDialog
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun DeviceDetailScreen(
    navController: NavHostController,
    viewModel: DeviceDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val context = LocalContext.current
    var nfcResultDialogVisible by remember { mutableStateOf(false) }
    var nfcRequestChangeSerialDialogVisible by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val installDeviceJson = navBackStackEntry?.arguments?.getString(ArgumentName.ARGU_INSTALL_DEVICE)

    LaunchedEffect(installDeviceJson) {
        if (installDeviceJson != null) {
            val installDevice = Gson().fromJson(installDeviceJson, InstallDevice::class.java)
            viewModel.initialize(installDevice)
        }
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

    DeviceDetailScreen(
        installDevice = state.installDevice,
        imageList = state.deviceImageList.sortedBy { it.first },
        nfcRequestChangeSerial = { nfcRequestChangeSerialDialogVisible = true },
    )

    NfcRequestChangeSerialDialog(
        visible = nfcRequestChangeSerialDialogVisible,
        header = "Change Serial",
        description = "enter new serial number (max ${state.installDevice.meterDeviceSn?.length ?: 12})",
        userInput = state.nfcRequestChangeSerialUserInput,
        onUserInputChange = viewModel::onTextChangeInChangeSerialDialog,
        onTagButtonClick = viewModel::nfcRequestChangeSerial,
        onResultDialogVisible = { nfcResultDialogVisible = true },
        onUserInputClear = viewModel::clearNfcRequestChangeSerialUserInput,
        onDismissRequest = { nfcRequestChangeSerialDialogVisible = false }
    )

    NfcResultDialog(
        visible = nfcResultDialogVisible,
        result = state.nfcResult,
        onDismissRequest = {
            nfcResultDialogVisible = false
            viewModel.clearNfcResult()
        }
    )
}

@Composable
private fun DeviceDetailScreen(
    installDevice: InstallDevice,
    imageList: List<Pair<Int, Any?>>,
    nfcRequestChangeSerial: () -> Unit,
) {
    // control nfcMenu expanded
    var isNfcMenuExpanded by remember { mutableStateOf(false) }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (
            deviceImagePager,
            footer,
            fab,
            nfcMenu,
        ) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(deviceImagePager) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(footer.top)
                    height = Dimension.fillToConstraints // apply to top bottom constraints
                }
                .verticalScroll(rememberScrollState())
        ) {
            DeviceImagePager(imageList)
            UserInfo(installDevice = installDevice)
            Spacer(modifier = Modifier.height(8.dp))
            TerminalInfo(installDevice = installDevice)
            Spacer(modifier = Modifier.height(8.dp))
            MeterInfo(installDevice = installDevice)
        }

        DeviceDetailFooter(
            modifier = Modifier.constrainAs(footer) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        NfcExtendedFab(
            modifier = Modifier.constrainAs(fab) {
                end.linkTo(parent.end, margin = Paddings.xlarge)
                bottom.linkTo(footer.top, margin = Paddings.xlarge)
            },
            isNfcMenuExpended = isNfcMenuExpanded,
            onClick = { isNfcMenuExpanded = !isNfcMenuExpanded },
        )

        NfcMenu(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .constrainAs(nfcMenu) {
                    top.linkTo(parent.top, margin = Paddings.medium)
                    end.linkTo(parent.end, margin = Paddings.medium)
                    bottom.linkTo(fab.top, margin = Paddings.xsmall)
                    height = Dimension.fillToConstraints
                },
            isVisible = isNfcMenuExpanded,
            onChangeSerialClick = nfcRequestChangeSerial
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceDetailScreenPreview() {
    RenewSmartSetTheme {
        Surface {
            DeviceDetailScreen(
                installDevice = InstallDevice(meterDeviceId = "HT-T-012345"),
                imageList = emptyList(),
                nfcRequestChangeSerial = {},
            )
        }
    }
}