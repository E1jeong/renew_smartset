package com.hitec.presentation.main.device_detail.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.hitec.presentation.component.dialog.BaseDialog
import com.hitec.presentation.component.dialog.model.DialogContent
import com.hitec.presentation.component.dialog.model.DialogText
import com.hitec.presentation.component.dialog.model.DialogTitle
import com.hitec.presentation.theme.RenewSmartSetTheme

@Composable
fun NfcResultDialog(
    visible: Boolean = false,
    result: String,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            BaseDialog(
                dialogTitle = DialogTitle.Header(text = "Nfc result"),
                dialogContent = DialogContent.Default(DialogText.Default(result)),
            )
        }
    }
}

@Preview
@Composable
fun NfcResultDialogPreview() {
    RenewSmartSetTheme {
        NfcResultDialog(
            visible = true,
            result = "test",
            onDismissRequest = {}
        )
    }
}