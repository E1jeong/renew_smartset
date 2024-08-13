package com.hitec.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.hitec.presentation.R
import com.hitec.presentation.component.dialog.BaseDialog
import com.hitec.presentation.component.dialog.model.DialogContent
import com.hitec.presentation.component.dialog.model.DialogText
import com.hitec.presentation.component.dialog.model.DialogTitle

@Composable
fun AndroidDeviceIdDialog(
    visible: Boolean = false,
    androidDeviceId: String,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            BaseDialog(
                dialogTitle = DialogTitle.Header(text = stringResource(id = R.string.android_device_id)),
                dialogContent = DialogContent.Default(DialogText.Default(androidDeviceId)),
            )
        }
    }
}