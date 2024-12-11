package com.hitec.presentation.main.as_report

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.gson.Gson
import com.hitec.domain.model.AsDevice
import com.hitec.presentation.R
import com.hitec.presentation.component.button.PrimaryButton
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme
import com.hitec.presentation.theme.backgroundGray0
import com.hitec.presentation.theme.backgroundGray2
import com.hitec.presentation.theme.backgroundGray5

@Composable
fun AsReportScreen(
    navController: NavHostController,
    viewModel: AsReportViewModel = hiltViewModel()
) {
    InitScreen(navController = navController, viewModel = viewModel)

    val state by viewModel.container.stateFlow.collectAsState()
    var selectedHandlingContent by remember { mutableStateOf("") }
    var selectedHandlingContentDetail by remember { mutableStateOf<List<String>>(emptyList()) }
    var onSelectedHandlingContentChange by remember { mutableStateOf(false) }

    AsReportScreen(
        state = state,
        asRequestComment = state.asRequestComment,
        onAsRequestCommentChange = viewModel::onAsRequestCommentChange,
        asHandlingMemo = state.asHandlingMemo,
        onAsHandlingMemoChange = viewModel::onAsHandlingMemoChange,
        handlingContentList = state.asCodeContent.mapNotNull { it.asCodeName },
        onHandlingContentSelect = { selected ->
            selectedHandlingContent = state.asCodeContent.find { it.asCodeName == selected }?.asCodeId.toString()
            selectedHandlingContentDetail = state.asCodeContentDetail
                .filter { it.asCodeFieldActionMain == selectedHandlingContent }
                .mapNotNull { it.asCodeName }
            viewModel.onHandlingContentChange(selected)
            onSelectedHandlingContentChange = true
        },
        handlingContentDetailList = selectedHandlingContentDetail,
        onHandlingContentDetailSelect = {
            viewModel.onHandlingContentDetailChange(it)
            onSelectedHandlingContentChange = false
        },
        onSelectedHandlingContentChange = onSelectedHandlingContentChange,
        onUploadButtonClick = viewModel::uploadAsReport
    )

    UploadResultDialog(
        visible = state.isUploadResultDialogVisible,
        result = state.uploadResult,
        onDismissRequest = viewModel::onUploadResultDialogDismiss
    )
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
    state: AsReportState,
    asRequestComment: String = "",
    onAsRequestCommentChange: (String) -> Unit,
    asHandlingMemo: String = "",
    onAsHandlingMemoChange: (String) -> Unit,
    handlingContentList: List<String>,
    onHandlingContentSelect: (String) -> Unit,
    handlingContentDetailList: List<String>,
    onHandlingContentDetailSelect: (String) -> Unit,
    onSelectedHandlingContentChange: Boolean,
    onUploadButtonClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.medium)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundGray0)
            ) {
                TextRow(contentTitle = "Report number", contentValue = state.asDevice.reportNo.toString())
                TextRow(
                    contentTitle = "State",
                    contentValue = if (state.asDevice.modTypeCd.toString() == "I") "request" else "complete"
                )
                TextRow(contentTitle = "Current date", contentValue = state.asDevice.receiptDt.toString())
                TextRow(contentTitle = "Handled by", contentValue = state.loginScreenInfo.id)
                TextRow(contentTitle = "Serial number", contentValue = state.asDevice.deviceSn.toString())
                TextRow(contentTitle = "First installation date", contentValue = state.asDevice.firstSetDt.toString())
                TextRow(contentTitle = "IMEI", contentValue = state.asDevice.nwk.toString())
                TextRow(contentTitle = "IMSI", contentValue = state.asDevice.cdmaNo.toString())
                TextRow(contentTitle = "Firmware", contentValue = state.asDevice.firmware.toString())
                TextRow(contentTitle = "House number", contentValue = state.asDevice.consumeHouseNo.toString())
                TextRow(contentTitle = "House owner", contentValue = state.asDevice.consumeHouseNm.toString())
                TextRow(
                    contentTitle = "House address",
                    contentValue = "${state.asDevice.areaBig.toString()} ${state.asDevice.setAreaAddr}"
                )
                TextRow(contentTitle = "Connected date", contentValue = state.asDevice.connectDtm.toString())
                TextRow(contentTitle = "Voltage", contentValue = "${state.asDevice.deviceBattery.toString()}V")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.medium)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundGray2)
            ) {
                HandlingContentDropdownMenu(
                    handlingContentList = handlingContentList,
                    onHandlingContentSelect = { onHandlingContentSelect(it) }
                )
                HandlingContentDetailDropdownMenu(
                    onSelectedHandlingContentChange = onSelectedHandlingContentChange,
                    handlingContentDetailList = handlingContentDetailList,
                    onHandlingContentDetailSelect = onHandlingContentDetailSelect,
                )
                CustomTextField(
                    modifier = Modifier
                        .padding(Paddings.small)
                        .fillMaxWidth(),
                    value = asHandlingMemo,
                    placeholder = "AS handling memo",
                    onValueChange = onAsHandlingMemoChange
                )
                CustomTextField(
                    modifier = Modifier
                        .padding(Paddings.small)
                        .fillMaxWidth(),
                    value = asRequestComment,
                    placeholder = "AS request comment",
                    onValueChange = onAsRequestCommentChange
                )
            }

            PrimaryButton(
                modifier = Modifier.padding(Paddings.medium),
                text = stringResource(id = R.string.upload),
                leadingIcon = LeadingIcon(Icons.Filled.Upload),
                onClick = onUploadButtonClick
            )
        }
    }
}

@Composable
private fun HandlingContentDropdownMenu(
    handlingContentList: List<String>,
    onHandlingContentSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("") }

    Log.e("!!@@", "check: $handlingContentList")

    Row(
        modifier = Modifier.padding(Paddings.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("handling content: ")

        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(0.dp, Paddings.medium, Paddings.medium, Paddings.medium)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (selected.isNotEmpty()) backgroundGray0 else Color.Transparent,
                            shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp)
                        )
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = if (selected.isNotEmpty()) backgroundGray0 else Color.Transparent,
                            shape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)
                        ),
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                )
            }

            DropdownMenu(
                modifier = Modifier.fillMaxWidth(0.5f),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                handlingContentList.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selected = option
                            expanded = false
                            onHandlingContentSelect(selected)
                        }
                    )

                    if (index < handlingContentList.size - 1) {
                        Divider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun HandlingContentDetailDropdownMenu(
    onSelectedHandlingContentChange: Boolean,
    handlingContentDetailList: List<String>,
    onHandlingContentDetailSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("") }
    Log.e("!!@@", "HandlingContentDetailDropdownMenu: $handlingContentDetailList")

    if (onSelectedHandlingContentChange) {
        selected = ""
    }

    Row(
        modifier = Modifier.padding(Paddings.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("detail content: ")

        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(0.dp, Paddings.medium, Paddings.medium, Paddings.medium)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (selected.isNotEmpty()) backgroundGray0 else Color.Transparent,
                            shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp)
                        )
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = if (selected.isNotEmpty()) backgroundGray0 else Color.Transparent,
                            shape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)
                        ),
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                )
            }

            DropdownMenu(
                modifier = Modifier.fillMaxWidth(0.5f),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                handlingContentDetailList.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selected = option
                            expanded = false
                            onHandlingContentDetailSelect(selected)
                        }
                    )

                    if (index < handlingContentDetailList.size - 1) {
                        Divider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomTextField(
    modifier: Modifier,
    value: String,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = value,
        placeholder = {
            Text(
                modifier = Modifier.alpha(0.5f),
                text = placeholder
            )
        },
        onValueChange = onValueChange,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundGray0,
            unfocusedContainerColor = backgroundGray0,
            disabledContainerColor = backgroundGray0,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        visualTransformation = visualTransformation
    )
}

@Composable
private fun TextRow(contentTitle: String, contentValue: String) {
    Row(
        modifier = Modifier.padding(Paddings.xsmall),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(text = contentTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = contentValue, fontSize = 12.sp, color = backgroundGray5)
    }
}

@Preview
@Composable
private fun AsReportScreenPreview() {
    RenewSmartSetTheme {
        AsReportScreen(
            state = AsReportState(),
            asRequestComment = "asRequestComment",
            onAsRequestCommentChange = {},
            asHandlingMemo = "asHandlingMemo",
            onAsHandlingMemoChange = {},
            handlingContentList = emptyList(),
            onHandlingContentSelect = {},
            handlingContentDetailList = emptyList(),
            onHandlingContentDetailSelect = {},
            onSelectedHandlingContentChange = false,
            onUploadButtonClick = {},
        )
    }
}