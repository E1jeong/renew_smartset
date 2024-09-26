package com.hitec.presentation.main.device_detail.dialog.nfc_request

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SendToMobile
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.hitec.presentation.component.dialog.BaseDialog
import com.hitec.presentation.component.dialog.model.DialogButton
import com.hitec.presentation.component.dialog.model.DialogButtonArrangement
import com.hitec.presentation.component.dialog.model.DialogContent
import com.hitec.presentation.component.dialog.model.DialogText
import com.hitec.presentation.component.dialog.model.DialogTitle
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.theme.RenewSmartSetTheme

@Composable
fun NfcRequestReadConfigDialog(
    visible: Boolean = false,
    onTagButtonClick: () -> Unit,
    onResultDialogVisible: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            BaseDialog(
                dialogTitle = DialogTitle.Header("Nfc Request"),
                dialogContent = DialogContent.Default(DialogText.Default("read config")),
                buttonArrangement = DialogButtonArrangement.Row(
                    listOf(
                        DialogButton.Primary(
                            title = "Tag",
                            leadingIcon = LeadingIcon(icon = Icons.Filled.SendToMobile),
                            action = {
                                onTagButtonClick()
                                onDismissRequest()
                                onResultDialogVisible()
                            }
                        ),
                        DialogButton.Primary(
                            title = "Close",
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
        NfcRequestReadConfigDialog(
            visible = true,
            onTagButtonClick = {},
            onResultDialogVisible = {},
            onDismissRequest = {}
        )
    }
}