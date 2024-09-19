package com.hitec.presentation.main.device_detail

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
import androidx.compose.ui.unit.dp
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.main.device_detail.component.InformationRow
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.backgroundGray0

@Composable
fun UserInfo(installDevice: InstallDevice) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Paddings.medium)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundGray0)
    ) {
        Column(modifier = Modifier.padding(horizontal = Paddings.medium)) {
            InformationRow(text = "UserInfo", style = MaterialTheme.typography.headlineMedium) //title
            SetDateInfo(text = "${installDevice.setDt} / ${installDevice.setInitDate}")
            HouseOwnerInfo(text = "${installDevice.consumeHouseNm}")
            HouseNumberInfo(text = "${installDevice.consumeHouseNo}")
            AddressInfo(text = "${installDevice.AreaBig} ${installDevice.setAreaAddr}")
            HouseCheckListInfo(text = "${installDevice.setPlaceDesc} ${installDevice.accountCheckNote}")
        }
    }
}

@Composable
fun HouseNumberInfo(text: String) {
    InformationRow(text = text)
}

@Composable
fun AddressInfo(text: String) {
    InformationRow(text = text)
}

@Composable
fun HouseOwnerInfo(text: String) {
    InformationRow(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun HouseCheckListInfo(text: String) {
    InformationRow(text = text)
}

@Composable
fun SetDateInfo(text: String) {
    InformationRow(text = text)
}