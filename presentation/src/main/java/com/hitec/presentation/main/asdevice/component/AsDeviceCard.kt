package com.hitec.presentation.main.asdevice.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hitec.domain.model.AsDevice
import com.hitec.presentation.R
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsDeviceCard(
    asDevice: AsDevice,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Paddings.medium)
            .shadow(elevation = 10.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(Paddings.large)) {
            Text(text = stringResource(id = R.string.card_content_user_house_number) + " ${asDevice.consumeHouseNo}")
            Text(text = stringResource(id = R.string.card_content_device_serial_number) + " ${asDevice.deviceSn}")
            Text(text = stringResource(id = R.string.card_content_imei) + " ${asDevice.nwk}")
            Text(text = stringResource(id = R.string.card_content_user_house_address) + " ${asDevice.setAreaAddr}")
        }
    }
}

@Preview
@Composable
private fun AsDeviceCardPreview() {
    RenewSmartSetTheme {
        AsDeviceCard(
            asDevice = AsDevice(meterDeviceId = "HT-1-12345"),
            onClick = {},
        )
    }
}

