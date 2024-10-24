package com.hitec.presentation.main.device_detail.dialog.nfc_request

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SendToMobile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hitec.presentation.R
import com.hitec.presentation.component.button.PrimaryButton
import com.hitec.presentation.component.dialog.wrapper.DialogContentStyle
import com.hitec.presentation.component.dialog.wrapper.LocalDialogContentStyle
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme

@Composable
fun NfcRequestUpdateFirmwareDialog(
    visible: Boolean = false,
    onSetUpdateMode: (String) -> Unit,
    userInputFirmware: String,
    onTextChange: (String) -> Unit,
    onUserInputClear: () -> Unit,
    onTagButtonClick: () -> Unit,
    onResultDialogVisible: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp

            var selectedUpdateMode by remember { mutableStateOf("FOTA") } // 상태 추가

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
                    onRadioButtonClick = { mode ->
                        selectedUpdateMode = mode
                        onSetUpdateMode(mode)
                    },
                    userInputFirmware = userInputFirmware,
                    onTextChange = onTextChange,
                    onUserInputClear = onUserInputClear,
                    isFotaSelected = selectedUpdateMode == "FOTA",
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
            text = stringResource(id = R.string.update_firmware),
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
    userInputFirmware: String,
    onTextChange: (String) -> Unit,
    onUserInputClear: () -> Unit,
    isFotaSelected: Boolean,
) {
    CompositionLocalProvider(
        LocalDialogContentStyle provides DialogContentStyle(
            textStyle = { MaterialTheme.typography.bodyLarge },
            contentTopPadding = Paddings.medium,
            contentBottomPadding = Paddings.medium
        )
    ) {
        Column {
            UpdateModeRadioButton(onRadioButtonClick = onRadioButtonClick)
            FirmwareTextField(
                enabled = !isFotaSelected,
                textFieldValue = userInputFirmware,
                onTextChanged = onTextChange,
                onTextFieldClear = onUserInputClear,
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
private fun UpdateModeRadioButton(
    onRadioButtonClick: (String) -> Unit
) {
    var selected by remember { mutableStateOf("FOTA") }

    Row(verticalAlignment = Alignment.CenterVertically) {
        ContentText("update mode: ")
        RadioButton(
            modifier = Modifier.size(8.dp),
            selected = selected == "FOTA",
            onClick = {
                selected = "FOTA"
                onRadioButtonClick(selected)
            }
        )
        ContentText("FOTA")
        Spacer(modifier = Modifier.width(Paddings.xlarge))
        RadioButton(
            modifier = Modifier.size(8.dp),
            selected = selected == "BSL",
            onClick = {
                selected = "BSL"
                onRadioButtonClick(selected)
            }
        )
        ContentText("BSL")
    }
}

@Composable
private fun FirmwareTextField(
    enabled: Boolean,
    textFieldValue: String,
    onTextChanged: (String) -> Unit,
    onTextFieldClear: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ContentText("firmware: ")
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.large, Paddings.xlarge, Paddings.large, Paddings.xlarge),
            value = textFieldValue,
            maxLines = 1,
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.3f),
                    text = "U331",
                )
            },
            onValueChange = onTextChanged,
            shape = RoundedCornerShape(8.dp),
            enabled = enabled,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            trailingIcon = {
                if (textFieldValue.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable { onTextFieldClear() },
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = stringResource(id = R.string.cancel)
                    )
                }
            }
        )
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
fun NfcRequestUpdateFirmwareDialogPreview() {
    RenewSmartSetTheme {
        NfcRequestUpdateFirmwareDialog(
            visible = true,
            onSetUpdateMode = {},
            userInputFirmware = "",
            onTextChange = {},
            onUserInputClear = {},
            onTagButtonClick = {},
            onResultDialogVisible = {},
            onDismissRequest = {},
        )
    }
}