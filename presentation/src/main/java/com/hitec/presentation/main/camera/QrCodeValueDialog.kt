package com.hitec.presentation.main.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.hitec.presentation.R
import com.hitec.presentation.component.dialog.BaseDialog
import com.hitec.presentation.component.dialog.model.DialogContent
import com.hitec.presentation.component.dialog.model.DialogText
import com.hitec.presentation.component.dialog.model.DialogTitle

@Composable
fun QrCodeValueDialog(
    visible: Boolean = false,
    qrCodeValue: String,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            BaseDialog(
                dialogTitle = DialogTitle.Header(text = stringResource(id = R.string.qrcode_value_dialog_title)),
                dialogContent = DialogContent.Default(DialogText.Default(qrCodeValue)),
            )
        }
    }
}