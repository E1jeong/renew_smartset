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
fun MeterInfo(installDevice: InstallDevice) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Paddings.medium)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundGray0)
    ) {
        Column(modifier = Modifier.padding(horizontal = Paddings.medium)) {
            InformationRow( //title
                text = stringResource(id = R.string.meter_info),
                style = MaterialTheme.typography.headlineMedium
            )
            MeterSerialNumberInfo(text = "${installDevice.meterSn1}")
            MeterCaliberInfo(text = "${installDevice.metercaliberCd1}mm")
            MeterCompanyInfo(text = "${installDevice.meterCompany1}")
        }
    }
}

@Composable
fun MeterSerialNumberInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun MeterCaliberInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun MeterCompanyInfo(text: String) {
    InformationRow(text = text, style = MaterialTheme.typography.bodyMedium)
}