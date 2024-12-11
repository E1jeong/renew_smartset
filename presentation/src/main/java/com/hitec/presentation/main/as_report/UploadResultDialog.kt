package com.hitec.presentation.main.as_report

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.hitec.presentation.component.dialog.BaseDialog
import com.hitec.presentation.component.dialog.model.DialogContent
import com.hitec.presentation.component.dialog.model.DialogText
import com.hitec.presentation.component.dialog.model.DialogTitle
import com.hitec.presentation.theme.RenewSmartSetTheme

@Composable
fun UploadResultDialog(
    visible: Boolean = false,
    result: String,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            BaseDialog(
                dialogTitle = DialogTitle.Header(text = "Upload result"),
                dialogContent = DialogContent.Left(DialogText.Default(result)),
            )
        }
    }
}

@Preview
@Composable
fun UploadResultDialogPreview() {
    RenewSmartSetTheme {
        UploadResultDialog(
            visible = true,
            result = "test",
            onDismissRequest = {}
        )
    }
}