package com.hitec.presentation.main.device_detail.dialog.nfc_request

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SendToMobile
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
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
    header: String,
    description: String = "",
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
                dialogTitle = DialogTitle.Header(header),
                dialogContent = DialogContent.Default(DialogText.Default(description)),
                textFieldValue = userInput,
                onTextChanged = onUserInputChange,
                buttons = listOf(
                    DialogButton.Primary(
                        title = "Tag",
                        leadingIcon = LeadingIcon(icon = Icons.Filled.SendToMobile),
                        action = {
                            onTagButtonClick()
                            onDismissRequest()
                            onResultDialogVisible()
                        }
                    ),
                    DialogButton.UnderlinedText(title = "Close", action = onDismissRequest)
                ),
                onTextFieldClear = onUserInputClear
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
            header = "Header",
            description = "Description",
            userInput = "Input",
            onUserInputChange = {},
            onTagButtonClick = {},
            onResultDialogVisible = {},
            onUserInputClear = {},
            onDismissRequest = {}
        )
    }
}