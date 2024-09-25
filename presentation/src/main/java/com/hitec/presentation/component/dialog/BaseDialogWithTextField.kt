package com.hitec.presentation.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SendToMobile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hitec.presentation.component.dialog.model.DialogButton
import com.hitec.presentation.component.dialog.model.DialogContent
import com.hitec.presentation.component.dialog.model.DialogText
import com.hitec.presentation.component.dialog.model.DialogTitle
import com.hitec.presentation.component.dialog.wrapper.DialogButtonsRow
import com.hitec.presentation.component.dialog.wrapper.DialogContentWrapper
import com.hitec.presentation.component.dialog.wrapper.DialogTitleWrapper
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme

@Composable
fun BaseDialogWithTextField(
    dialogTitle: DialogTitle? = null,
    dialogContent: DialogContent? = null,
    buttons: List<DialogButton>? = null,
    textFieldValue: String,
    onTextFieldClear: () -> Unit,
    onTextChanged: (String) -> Unit,
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Paddings.none),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            dialogTitle?.let {
                DialogTitleWrapper(it)
            }
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .padding(Paddings.xlarge)
            ) {
                dialogContent?.let { DialogContentWrapper(it) }
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Paddings.xlarge),
                    value = textFieldValue,
                    maxLines = 1,
                    placeholder = {
                        Text(
                            modifier = Modifier.alpha(0.5f),
                            text = "NL1234567890",
                        )
                    },
                    onValueChange = onTextChanged,
                    shape = RoundedCornerShape(8.dp),
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
                                contentDescription = "Cancel"
                            )
                        }
                    }
                )
                buttons?.let { DialogButtonsRow(it) }
            }
        }
    }
}

@Preview
@Composable
fun BaseDialogWithTextFieldPreview() {
    RenewSmartSetTheme {
        BaseDialogWithTextField(
            dialogTitle = DialogTitle.Header("header"),
            dialogContent = DialogContent.Default(DialogText.Default("description")),
            textFieldValue = "input",
            onTextFieldClear = {},
            onTextChanged = {},
            buttons = listOf(
                DialogButton.Primary(
                    title = "Tag",
                    leadingIcon = LeadingIcon(icon = Icons.Filled.SendToMobile),
                    action = {}
                ),
                DialogButton.Primary(
                    title = "Close",
                    leadingIcon = LeadingIcon(icon = Icons.Filled.Close),
                    action = {}
                )
            )
        )
    }
}