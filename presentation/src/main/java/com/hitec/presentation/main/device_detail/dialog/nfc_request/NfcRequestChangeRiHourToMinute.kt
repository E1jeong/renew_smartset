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
fun NfcRequestChangeRiHourToMinuteDialog(
    visible: Boolean = false,
    userInput: String,
    onUserInputChange: (String) -> Unit,
    onUserInputClear: () -> Unit,
    onTagButtonClick: () -> Unit,
    onResultDialogVisible: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            BaseDialogWithTextField(
                dialogTitle = DialogTitle.Header(text = stringResource(id = R.string.change_ri_hour_to_minute)),
                dialogContent = DialogContent.Default(dialogText = DialogText.Default("minute (0~59), 0: return to hour")),
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
                placeholder = "0~59"
            )
        }
    }
}

@Preview
@Composable
fun NfcRequestChangeRiHourToMinuteDialogPreview() {
    RenewSmartSetTheme {
        NfcRequestChangeRiHourToMinuteDialog(
            visible = true,
            userInput = "Input",
            onUserInputChange = {},
            onTagButtonClick = {},
            onResultDialogVisible = {},
            onUserInputClear = {},
            onDismissRequest = {}
        )
    }
}