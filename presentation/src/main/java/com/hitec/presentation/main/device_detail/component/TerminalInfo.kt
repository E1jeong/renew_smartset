package com.hitec.presentation.main.device_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.R
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.backgroundGray0


@Composable
fun TerminalInfo(installDevice: InstallDevice) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Paddings.medium)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundGray0)
    ) {
        Column(modifier = Modifier.padding(horizontal = Paddings.medium)) {
            InformationRow( //title
                text = stringResource(id = R.string.terminal_info),
                style = MaterialTheme.typography.headlineMedium
            )
            SerialNumberInfo(text = "${installDevice.meterDeviceSn}")
            ImeiInfo(text = "${installDevice.nwk}")
            CtnInfo(text = "${installDevice.cdmaNo}")
            FirmwareVersionInfo(text = "${installDevice.firmware}")
            ServerIpPortInfo(text = "${installDevice.serverAddr1}:${installDevice.serverPort1}")
            LatitudeInfo(text = "${installDevice.gpsLatitude}")
            LongitudeInfo(text = "${installDevice.gpsLongitude}")
            MeterIntervalInfo(text = "${installDevice.meterIntervalTime}")
            ReportIntervalInfo(text = "${installDevice.reportIntervalTime}")
        }
    }
}

@Composable
fun SerialNumberInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun ImeiInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun CtnInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun FirmwareVersionInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun ServerIpPortInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun LatitudeInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun LongitudeInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun MeterIntervalInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun ReportIntervalInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}