package com.hitec.presentation.main.device_detail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun InformationRow(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            style = style,
            color = color
        )
    }
}
