package com.hitec.presentation.main.device_detail

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.R
import com.hitec.presentation.main.as_report.UploadResultDialog
import com.hitec.presentation.main.device_detail.component.MeterInfo
import com.hitec.presentation.main.device_detail.component.NfcExtendedFab
import com.hitec.presentation.main.device_detail.component.NfcMenu
import com.hitec.presentation.main.device_detail.component.TerminalInfo
import com.hitec.presentation.main.device_detail.component.UserInfo
import com.hitec.presentation.main.device_detail.dialog.nfc_request.NfcRequestChangeRiHourToMinuteDialog
import com.hitec.presentation.main.device_detail.dialog.nfc_request.NfcRequestChangeSerialDialog
import com.hitec.presentation.main.device_detail.dialog.nfc_request.NfcRequestReadPeriodDataDialog
import com.hitec.presentation.main.device_detail.dialog.nfc_request.NfcRequestUpdateFirmwareBslDialog
import com.hitec.presentation.main.device_detail.dialog.nfc_request.NfcRequestWriteConfigDialog
import com.hitec.presentation.main.device_detail.dialog.nfc_response.NfcResultDialog
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme
import com.hitec.presentation.util.PermissionState
import com.hitec.presentation.util.permissionRequest
import org.orbitmvi.orbit.compose.collectSideEffect

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun DeviceDetailScreen(
    navController: NavHostController,
    viewModel: DeviceDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val context = LocalContext.current

    var nfcResultDialogVisible by remember { mutableStateOf(false) }
    var nfcRequestChangeSerialDialogVisible by remember { mutableStateOf(false) }
    var nfcRequestWriteConfigDialogVisible by remember { mutableStateOf(false) }
    var nfcRequestUpdateFirmwareDialogVisible by remember { mutableStateOf(false) }
    var nfcRequestChangeRiHourToMinuteDialogVisible by remember { mutableStateOf(false) }
    var nfcRequestReadPeriodDataDialogVisible by remember { mutableStateOf(false) }

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

    val requiredPermissions = listOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
    val permissionState = permissionRequest(
        permissions = requiredPermissions,
        rationaleTitle = stringResource(R.string.rationale_title_text),
        rationaleText = stringResource(R.string.rationale_content_text)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        when (permissionState) {
            PermissionState.Granted -> {
                DeviceDetailScreen(
                    installDevice = state.installDevice,
                    imageList = state.deviceImageList.sortedBy { it.first },
                    nfcRequestChangeSerial = { nfcRequestChangeSerialDialogVisible = true },
                    nfcRequestReadConfig = {
                        nfcResultDialogVisible = true
                        viewModel.nfcRequestReadConfig()
                    },
                    nfcRequestWriteConfig = { nfcRequestWriteConfigDialogVisible = true },
                    nfcRequestSetSleep = {
                        nfcResultDialogVisible = true
                        viewModel.nfcRequestSetSleep()
                    },
                    nfcRequestSetActive = {
                        nfcResultDialogVisible = true
                        viewModel.nfcRequestSetActive()
                    },
                    nfcRequestResetDevice = {
                        nfcResultDialogVisible = true
                        viewModel.nfcRequestResetDevice()
                    },
                    nfcRequestReadMeter = {
                        nfcResultDialogVisible = true
                        viewModel.nfcRequestReadMeter()
                    },
                    nfcRequestReqComm = {
                        nfcResultDialogVisible = true
                        viewModel.nfcRequestReqComm()
                    },
                    nfcRequestCheckComm = {
                        nfcResultDialogVisible = true
                        viewModel.nfcRequestCheckComm()
                    },
                    nfcRequestUpdateFirmwareBsl = { nfcRequestUpdateFirmwareDialogVisible = true },
                    nfcRequestUpdateFirmwareFota = {
                        nfcResultDialogVisible = true
                        viewModel.nfcRequestUpdateFirmwareFota()
                    },
                    nfcRequestChangeRiHourToMinute = { nfcRequestChangeRiHourToMinuteDialogVisible = true },
                    nfcRequestReadPeriodData = { nfcRequestReadPeriodDataDialogVisible = true },
                    onUploadButtonClick = viewModel::uploadInstallDevice
                )
            }

            PermissionState.Denied, PermissionState.NeedsSpecialPermission -> {
                Text(text = stringResource(R.string.rationale_content_text))
            }
        }
    }

    NfcRequestChangeSerialDialog(
        visible = nfcRequestChangeSerialDialogVisible,
        maxLength = state.installDevice.meterDeviceSn?.length ?: 12,
        userInput = state.nfcRequestChangeSerialUserInput,
        onUserInputChange = viewModel::onTextChangeInChangeSerialDialog,
        onTagButtonClick = viewModel::nfcRequestChangeSerial,
        onResultDialogVisible = { nfcResultDialogVisible = true },
        onUserInputClear = viewModel::clearNfcRequestChangeSerialUserInput,
        onDismissRequest = { nfcRequestChangeSerialDialogVisible = false }
    )

    NfcRequestWriteConfigDialog(
        visible = nfcRequestWriteConfigDialogVisible,
        onSetTerminalProtocol = { protocol -> viewModel.setTerminalProtocolInWriteConfig(protocol) },
        onSetIpPort = { ipPort -> viewModel.setIpPortInWriteConfig(ipPort) },
        onSetMeterInterval = { meterInterval -> viewModel.setMeterIntervalInWriteConfig(meterInterval) },
        onSetReportInterval = { reportInterval -> viewModel.setReportIntervalInWriteConfig(reportInterval) },
        onTagButtonClick = viewModel::nfcRequestWriteConfig,
        onResultDialogVisible = { nfcResultDialogVisible = true },
        onDismissRequest = { nfcRequestWriteConfigDialogVisible = false }
    )

    NfcRequestUpdateFirmwareBslDialog(
        visible = nfcRequestUpdateFirmwareDialogVisible,
        userInputFirmware = state.userInputFirmwareInUpdateFirmware,
        onTextChange = viewModel::onTextChangeInUpdateFirmware,
        onUserInputClear = viewModel::onClearUserInputFirmwareInUpdateFirmware,
        onTagButtonClick = viewModel::nfcRequestUpdateFirmwareBsl,
        onResultDialogVisible = { nfcResultDialogVisible = true },
        onDismissRequest = { nfcRequestUpdateFirmwareDialogVisible = false }
    )

    NfcRequestChangeRiHourToMinuteDialog(
        visible = nfcRequestChangeRiHourToMinuteDialogVisible,
        userInput = state.userInputMinuteInChangeRiHourToMinute,
        onUserInputChange = viewModel::onTextChangeInChangeRiHourToMinute,
        onUserInputClear = viewModel::onClearUserInputMinuteInChangeRiHourToMinute,
        onTagButtonClick = viewModel::nfcRequestChangeRiHourToMinute,
        onResultDialogVisible = { nfcResultDialogVisible = true },
        onDismissRequest = { nfcRequestChangeRiHourToMinuteDialogVisible = false }
    )

    NfcRequestReadPeriodDataDialog(
        visible = nfcRequestReadPeriodDataDialogVisible,
        startDate = state.startDate,
        endDate = state.endDate,
        onStartDateSelected = viewModel::setStartDate,
        onEndDateSelected = viewModel::setEndDate,
        onTagButtonClick = viewModel::nfcRequestReadPeriodData,
        onResultDialogVisible = { nfcResultDialogVisible = true },
        onDismissRequest = { nfcRequestReadPeriodDataDialogVisible = false }
    )

    NfcResultDialog(
        visible = nfcResultDialogVisible,
        result = state.nfcResult,
        onDismissRequest = {
            nfcResultDialogVisible = false
            viewModel.clearNfcResult()
        }
    )

    UploadResultDialog(
        visible = state.isUploadResultDialogVisible,
        result = state.uploadResult,
        onDismissRequest = viewModel::onUploadResultDialogDismiss
    )
}

@Composable
private fun DeviceDetailScreen(
    installDevice: InstallDevice,
    imageList: List<Pair<Int, Any?>>,
    nfcRequestChangeSerial: () -> Unit,
    nfcRequestReadConfig: () -> Unit,
    nfcRequestWriteConfig: () -> Unit,
    nfcRequestSetSleep: () -> Unit,
    nfcRequestSetActive: () -> Unit,
    nfcRequestResetDevice: () -> Unit,
    nfcRequestReadMeter: () -> Unit,
    nfcRequestReqComm: () -> Unit,
    nfcRequestCheckComm: () -> Unit,
    nfcRequestUpdateFirmwareBsl: () -> Unit,
    nfcRequestUpdateFirmwareFota: () -> Unit,
    nfcRequestChangeRiHourToMinute: () -> Unit,
    nfcRequestReadPeriodData: () -> Unit,
    onUploadButtonClick: () -> Unit,
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
            ConsumerHouseMap(installDevice = installDevice)
            Spacer(modifier = Modifier.height(8.dp))
            UserInfo(installDevice = installDevice)
            Spacer(modifier = Modifier.height(8.dp))
            TerminalInfo(installDevice = installDevice)
            Spacer(modifier = Modifier.height(8.dp))
            MeterInfo(installDevice = installDevice)
        }

        DeviceDetailFooter(
            onUploadButtonClick = onUploadButtonClick,
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
            onChangeSerialClick = nfcRequestChangeSerial,
            onReadConfigClick = nfcRequestReadConfig,
            onWriteConfigClick = nfcRequestWriteConfig,
            onSetSleepClick = nfcRequestSetSleep,
            onSetActiveClick = nfcRequestSetActive,
            onResetDeviceClick = nfcRequestResetDevice,
            onReadMeterClick = nfcRequestReadMeter,
            onReqCommClick = nfcRequestReqComm,
            onCheckCommClick = nfcRequestCheckComm,
            onUpdateFirmwareBslClick = nfcRequestUpdateFirmwareBsl,
            onUpdateFirmwareFotaClick = nfcRequestUpdateFirmwareFota,
            onChangeRiHourToMinuteClick = nfcRequestChangeRiHourToMinute,
            onReadPeriodDataClick = nfcRequestReadPeriodData,
        )
    }
}

@Composable
private fun ConsumerHouseMap(installDevice: InstallDevice) {
    val consumerHousePosition = LatLng(
        installDevice.gpsLatitude?.toDouble() ?: 37.528529,
        installDevice.gpsLongitude?.toDouble() ?: 127.168089
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(consumerHousePosition, 15f)
    }

    val markerState = rememberMarkerState(position = consumerHousePosition)

    LaunchedEffect(consumerHousePosition) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(consumerHousePosition, 15f),
            500 // 애니메이션 지속 시간(ms)
        )
        markerState.position = consumerHousePosition
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = Paddings.medium)
            .clip(RoundedCornerShape(8.dp))
    ) {
        GoogleMap(cameraPositionState = cameraPositionState) {
            AdvancedMarker(state = markerState)
        }
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
                nfcRequestReadConfig = {},
                nfcRequestWriteConfig = {},
                nfcRequestSetSleep = {},
                nfcRequestSetActive = {},
                nfcRequestResetDevice = {},
                nfcRequestReadMeter = {},
                nfcRequestReqComm = {},
                nfcRequestCheckComm = {},
                nfcRequestUpdateFirmwareBsl = {},
                nfcRequestUpdateFirmwareFota = {},
                nfcRequestChangeRiHourToMinute = {},
                nfcRequestReadPeriodData = {},
                onUploadButtonClick = {},
            )
        }
    }
}