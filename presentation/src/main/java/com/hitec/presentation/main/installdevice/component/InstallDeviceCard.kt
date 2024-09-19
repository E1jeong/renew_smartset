package com.hitec.presentation.main.installdevice.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallDeviceCard(
    installDevice: InstallDevice,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Paddings.medium)
            .shadow(elevation = 10.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(Paddings.large)) {
            Text(text = "User house number: ${installDevice.consumeHouseNo}")
            Text(text = "Device serial number: ${installDevice.meterDeviceSn}")
            Text(text = "IMEI: ${installDevice.nwk}")
            Text(text = "User House address: ${installDevice.setAreaAddr}")
        }
    }
}

@Preview
@Composable
fun InstallDeviceCardPreview() {
    RenewSmartSetTheme {
        InstallDeviceCard(
            InstallDevice(
                "123",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
            ),
            {}
        )
    }
}