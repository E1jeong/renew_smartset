package com.hitec.presentation.main.device_detail.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PermDeviceInformation
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hitec.presentation.R

@Composable
fun NfcExtendedFab(
    modifier: Modifier = Modifier,
    isNfcMenuExpended: Boolean,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        text = { Text(text = stringResource(id = R.string.nfc)) },
        expanded = isNfcMenuExpended,
        shape = RoundedCornerShape(100),
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Filled.PermDeviceInformation,
                contentDescription = stringResource(id = R.string.nfc)
            )
        },
    )
}