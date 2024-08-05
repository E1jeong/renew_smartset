package com.hitec.presentation.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hitec.presentation.component.button.PrimaryButton
import com.hitec.presentation.component.button.SecondaryBorderlessButton
import com.hitec.presentation.component.button.SecondaryButton
import com.hitec.presentation.component.button.UnderlinedTextButton
import com.hitec.presentation.component.dialog.model.DialogButton
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.theme.Paddings


@Composable
fun DialogButtonsColumn(
    buttons: List<DialogButton>?
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttons?.forEachIndexed { index, dialogButton ->
            if (index > 0) {
                Spacer(modifier = Modifier.height(Paddings.large))
            }
            when (dialogButton) {
                is DialogButton.Primary -> {
                    PrimaryButton(
                        text = dialogButton.title,
                        leadingIcon = dialogButton.leadingIcon?.let {
                            LeadingIcon(
                                icon = it.icon,
                                iconContentDescription = it.iconContentDescription ?: ""
                            )
                        }
                    ) { dialogButton.action?.invoke() }
                }

                is DialogButton.Secondary -> {
                    SecondaryButton(
                        text = dialogButton.title
                    ) { dialogButton.action?.invoke() }
                }

                is DialogButton.SecondaryBorderless -> {
                    SecondaryBorderlessButton(
                        text = dialogButton.title
                    ) { dialogButton.action?.invoke() }
                }

                is DialogButton.UnderlinedText -> {
                    UnderlinedTextButton(
                        text = dialogButton.title
                    ) { dialogButton.action?.invoke() }
                }
            }
        }
    }
}
