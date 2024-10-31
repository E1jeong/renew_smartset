package com.hitec.presentation.main.device_detail.dialog.nfc_request

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SendToMobile
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.hitec.presentation.R
import com.hitec.presentation.component.dialog.BaseDialog
import com.hitec.presentation.component.dialog.model.DialogButton
import com.hitec.presentation.component.dialog.model.DialogButtonArrangement
import com.hitec.presentation.component.dialog.model.DialogContent
import com.hitec.presentation.component.dialog.model.DialogText
import com.hitec.presentation.component.dialog.model.DialogTitle
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.theme.RenewSmartSetTheme

//2024.10.31 this dialog don't use because unnecessary
@Composable
fun NfcRequestDialog(
    visible: Boolean = false,
    requestContent: Pair<String, () -> Unit>,
    onResultDialogVisible: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            BaseDialog(
                dialogTitle = DialogTitle.Header(text = stringResource(id = R.string.nfc_request)),
                dialogContent = DialogContent.Default(DialogText.Default(text = requestContent.first)),
                buttonArrangement = DialogButtonArrangement.Row(
                    listOf(
                        DialogButton.Primary(
                            title = stringResource(id = R.string.tag),
                            leadingIcon = LeadingIcon(icon = Icons.Filled.SendToMobile),
                            action = {
                                requestContent.second()
                                onDismissRequest()
                                onResultDialogVisible()
                            }
                        ),
                        DialogButton.Primary(
                            title = stringResource(id = R.string.close),
                            leadingIcon = LeadingIcon(icon = Icons.Filled.Close),
                            action = onDismissRequest
                        )
                    )
                )
            )
        }
    }
}

@Preview
@Composable
fun NfcRequestDialogPreview() {
    RenewSmartSetTheme {
        NfcRequestDialog(
            visible = true,
            requestContent = Pair("read config") {},
            onResultDialogVisible = {},
            onDismissRequest = {}
        )
    }
}