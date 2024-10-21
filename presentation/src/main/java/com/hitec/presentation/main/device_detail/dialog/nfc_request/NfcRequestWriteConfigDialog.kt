package com.hitec.presentation.main.device_detail.dialog.nfc_request

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SendToMobile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hitec.presentation.R
import com.hitec.presentation.component.button.PrimaryButton
import com.hitec.presentation.component.dialog.wrapper.DialogContentStyle
import com.hitec.presentation.component.dialog.wrapper.LocalDialogContentStyle
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme

/*
 * This dialog is unique so don't use basic dialog
 */
@Composable
fun NfcRequestWriteConfigDialog(
    visible: Boolean = false,
    onSetTerminalProtocol: (String) -> Unit,
    onSetIpPort: (String) -> Unit,
    onSetMeterInterval: (String) -> Unit,
    onSetReportInterval: (String) -> Unit,
    onTagButtonClick: () -> Unit,
    onResultDialogVisible: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = screenHeight * 0.8f),
                elevation = CardDefaults.cardElevation(defaultElevation = Paddings.none),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                shape = MaterialTheme.shapes.large
            ) {
                Header()
                Content(
                    onRadioButtonClick = onSetTerminalProtocol,
                    onIpPortSelect = onSetIpPort,
                    onMeterIntervalSelect = onSetMeterInterval,
                    onReportIntervalSelect = onSetReportInterval
                )
                Footer(
                    onTagButtonClick = onTagButtonClick,
                    onResultDialogVisible = onResultDialogVisible,
                    onDismissRequest = onDismissRequest
                )
            }
        }
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(Paddings.large),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.write_config),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Composable
private fun Content(
    onRadioButtonClick: (String) -> Unit,
    onIpPortSelect: (String) -> Unit,
    onMeterIntervalSelect: (String) -> Unit,
    onReportIntervalSelect: (String) -> Unit
) {
    CompositionLocalProvider(
        LocalDialogContentStyle provides DialogContentStyle(
            textStyle = { MaterialTheme.typography.bodyLarge },
            contentTopPadding = Paddings.medium,
            contentBottomPadding = Paddings.medium
        )
    ) {
        Column {
            TerminalProtocolRadioButton(onRadioButtonClick = onRadioButtonClick)
            IpPortDropdownMenu(onIpPortSelect = onIpPortSelect)
            IntervalDropdownMenu(
                onMeterIntervalSelect = onMeterIntervalSelect,
                onReportIntervalSelect = onReportIntervalSelect
            )
        }
    }
}

@Composable
private fun ContentText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .padding(
                top = LocalDialogContentStyle.current.contentTopPadding,
                bottom = LocalDialogContentStyle.current.contentBottomPadding,
                start = Paddings.medium,
                end = Paddings.medium
            ),
        textAlign = TextAlign.Start,
        style = LocalDialogContentStyle.current.textStyle.invoke()
    )
}

@Composable
private fun TerminalProtocolRadioButton(
    onRadioButtonClick: (String) -> Unit
) {
    var selected by remember { mutableStateOf("1.6") }

    Row(verticalAlignment = Alignment.CenterVertically) {
        ContentText("protocol: ")
        RadioButton(
            modifier = Modifier.size(8.dp),
            selected = selected == "1.6",
            onClick = {
                selected = "1.6"
                onRadioButtonClick(selected)
            }
        )
        ContentText("1.6")
        Spacer(modifier = Modifier.width(Paddings.xlarge))
        RadioButton(
            modifier = Modifier.size(8.dp),
            selected = selected == "1.7",
            onClick = {
                selected = "1.7"
                onRadioButtonClick(selected)
            }
        )
        ContentText("1.7")
    }
}

@Composable
private fun IpPortDropdownMenu(
    onIpPortSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("LG Business") }
    val menus = listOf("LG Business", "LG Dev", "KT Business", "KT Dev")

    Row(verticalAlignment = Alignment.CenterVertically) {
        ContentText("ip/port: ")

        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(0.dp, Paddings.medium, Paddings.medium, Paddings.medium)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp))
                    .padding(horizontal = Paddings.small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ContentText(text = selected)
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                modifier = Modifier.fillMaxWidth(0.5f),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                menus.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { ContentText(option) },
                        onClick = {
                            selected = option
                            expanded = false
                            onIpPortSelect(selected)
                        }
                    )

                    if (index < menus.size - 1) {
                        Divider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun IntervalDropdownMenu(
    onMeterIntervalSelect: (String) -> Unit,
    onReportIntervalSelect: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        MeteringIntervalDropdownMenu(onMeterIntervalSelect = onMeterIntervalSelect)
        ReportIntervalDropdownMenu(onReportIntervalSelect = onReportIntervalSelect)
    }
}

@Composable
private fun RowScope.MeteringIntervalDropdownMenu(
    onMeterIntervalSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("1") }
    val menus = listOf("1", "2", "3", "4", "6", "8", "12", "24")

    Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContentText("meter")
        Icon(imageVector = Icons.Filled.AccessTime, contentDescription = "")

        Box(modifier = Modifier
            .clickable { expanded = true }
            .padding(Paddings.medium)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp))
                    .padding(horizontal = Paddings.small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ContentText(text = selected)
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                modifier = Modifier.fillMaxWidth(0.15f),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                menus.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        modifier = Modifier.height(24.dp),
                        text = { Text(text = option, fontSize = 12.sp) },
                        onClick = {
                            selected = option
                            expanded = false
                            onMeterIntervalSelect(selected)
                        }
                    )

                    if (index < menus.size - 1) {
                        Divider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.ReportIntervalDropdownMenu(
    onReportIntervalSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("6") }
    val menus = listOf("1", "2", "3", "4", "6", "8", "12", "24")

    Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContentText("report")
        Icon(imageVector = Icons.Filled.AccessTime, contentDescription = "")

        Box(modifier = Modifier
            .clickable { expanded = true }
            .padding(Paddings.medium)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp))
                    .padding(horizontal = Paddings.small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ContentText(text = selected)
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                modifier = Modifier.fillMaxWidth(0.15f),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                menus.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        modifier = Modifier.height(24.dp),
                        text = { Text(text = option, fontSize = 12.sp) },
                        onClick = {
                            selected = option
                            expanded = false
                            onReportIntervalSelect(selected)
                        }
                    )

                    if (index < menus.size - 1) {
                        Divider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun Footer(
    onTagButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onResultDialogVisible: () -> Unit
) {
    Row(
        modifier = Modifier.padding(Paddings.large),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryButton(
            modifier = Modifier.weight(1f),
            text = "Tag",
            leadingIcon = LeadingIcon(Icons.Filled.SendToMobile, null),
            onClick = {
                onTagButtonClick()
                onResultDialogVisible()
                onDismissRequest()
            }
        )
        Spacer(modifier = Modifier.width(Paddings.large))
        PrimaryButton(
            modifier = Modifier.weight(1f),
            text = "Close",
            leadingIcon = LeadingIcon(Icons.Filled.Close, null),
            onClick = onDismissRequest
        )
    }
}

@Preview
@Composable
fun NfcRequestWriteConfigDialogPreview() {
    RenewSmartSetTheme {
        NfcRequestWriteConfigDialog(
            visible = true,
            onSetTerminalProtocol = {},
            onSetIpPort = {},
            onSetMeterInterval = {},
            onSetReportInterval = {},
            onTagButtonClick = {},
            onResultDialogVisible = {},
            onDismissRequest = {},
        )
    }
}