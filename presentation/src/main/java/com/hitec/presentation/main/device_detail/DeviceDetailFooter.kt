package com.hitec.presentation.main.device_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hitec.presentation.R
import com.hitec.presentation.component.button.PrimaryButton
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.theme.Paddings

@Composable
fun DeviceDetailFooter(
    onUploadButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Paddings.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        PrimaryButton(
            modifier = Modifier
                .weight(1f)
                .padding(Paddings.xsmall),
            text = stringResource(id = R.string.upload),
            leadingIcon = LeadingIcon(Icons.Filled.Upload),
            onClick = onUploadButtonClick
        )
    }
}