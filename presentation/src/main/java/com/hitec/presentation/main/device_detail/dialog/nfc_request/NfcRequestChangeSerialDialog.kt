package com.hitec.presentation.main.device_detail.dialog.nfc_request

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SendToMobile
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.hitec.presentation.R
import com.hitec.presentation.component.dialog.BaseDialogWithTextField
import com.hitec.presentation.component.dialog.model.DialogButton
import com.hitec.presentation.component.dialog.model.DialogContent
import com.hitec.presentation.component.dialog.model.DialogText
import com.hitec.presentation.component.dialog.model.DialogTitle
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.theme.RenewSmartSetTheme

@Composable
fun NfcRequestChangeSerialDialog(
    visible: Boolean = false,
    maxLength: Int,
    userInput: String,
    onUserInputChange: (String) -> Unit,
    onTagButtonClick: () -> Unit,
    onResultDialogVisible: () -> Unit,
    onUserInputClear: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            BaseDialogWithTextField(
                dialogTitle = DialogTitle.Header(text = stringResource(id = R.string.change_serial)),
                dialogContent = DialogContent.Default(
                    DialogText.Default(
                        text = stringResource(id = R.string.nfc_request_change_serial_description) + " (max $maxLength)"
                    )
                ),
                textFieldValue = userInput,
                onTextChanged = onUserInputChange,
                buttons = listOf(
                    DialogButton.Primary(
                        title = stringResource(id = R.string.tag),
                        leadingIcon = LeadingIcon(icon = Icons.Filled.SendToMobile),
                        action = {
                            onTagButtonClick()
                            onDismissRequest()
                            onResultDialogVisible()
                        }
                    ),
                    DialogButton.Primary(
                        title = stringResource(id = R.string.close),
                        leadingIcon = LeadingIcon(icon = Icons.Filled.Close),
                        action = onDismissRequest
                    )
                ),
                onTextFieldClear = onUserInputClear,
                placeholder = "NL1234567890"
            )
        }
    }
}

@Preview
@Composable
fun NfcRequestChangeSerialDialogPreview() {
    RenewSmartSetTheme {
        NfcRequestChangeSerialDialog(
            visible = true,
            maxLength = 12,
            userInput = "Input",
            onUserInputChange = {},
            onTagButtonClick = {},
            onResultDialogVisible = {},
            onUserInputClear = {},
            onDismissRequest = {}
        )
    }
}