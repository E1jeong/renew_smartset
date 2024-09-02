package com.hitec.presentation.main.device_detail

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.R
import com.hitec.presentation.component.button.PrimaryButton
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme
import com.hitec.presentation.theme.backgroundGray0
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun DeviceDetailScreen(
    navController: NavHostController,
    viewModel: DeviceDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val installDeviceJson =
        navBackStackEntry?.arguments?.getString(ArgumentName.ARGU_INSTALL_DEVICE)

    LaunchedEffect(installDeviceJson) {
        if (installDeviceJson != null) {
            val installDevice = Gson().fromJson(installDeviceJson, InstallDevice::class.java)
            viewModel.initialize(installDevice)
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is DeviceDetailSideEffect.Toast -> {
                Toast.makeText(
                    context,
                    sideEffect.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    DeviceDetailScreen(
        installDevice = state.installDevice,
        imageList = state.deviceImageList.sortedBy { it.first }
    )
}

@Composable
private fun DeviceDetailScreen(
    installDevice: InstallDevice?,
    imageList: List<Pair<Int, Any?>>,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (content, footer) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(footer.top)
                    height = Dimension.fillToConstraints // 상하 제약에 맞춰 높이를 설정
                }
                .verticalScroll(rememberScrollState())
        ) {
            if (imageList.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Paddings.large),
                    text = stringResource(id = R.string.no_image_available),
                    textAlign = TextAlign.Center,
                )
            } else {
                ImageSliderWithIndicator(imageList)
            }

            Spacer(modifier = Modifier.height(16.dp))
            UserInfo(installDevice = installDevice)
            Spacer(modifier = Modifier.height(8.dp))
            TerminalInfo(installDevice = installDevice)
            Spacer(modifier = Modifier.height(8.dp))
            MeterInfo(installDevice = installDevice)
        }

        DeviceDetailFooter(
            modifier = Modifier
                .constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSliderWithIndicator(imageList: List<Pair<Int, Any?>>) {
    val pagerState = rememberPagerState(pageCount = { imageList.size })
    val imagesType = imageList.map { it.first }
    val images = imageList.map { it.second }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Paddings.medium)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Slider image $page",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(shape = RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        //Image type
        if (pagerState.currentPage in imageList.indices) {
            Text(
                modifier = Modifier
                    .padding(top = Paddings.small)
                    .align(Alignment.TopCenter),
                text = ImageManager.parsePhotoTypeCd(imagesType[pagerState.currentPage]),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }

        // Indicator
        Row(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(imageList.size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                Box(
                    modifier = Modifier
                        .padding(Paddings.xsmall)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun UserInfo(installDevice: InstallDevice?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Paddings.medium)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundGray0)
    ) {
        Column(modifier = Modifier.padding(horizontal = Paddings.medium)) {
            Text(
                text = "UserInfo",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            SetDateInfo(text = "${installDevice?.setDt} / ${installDevice?.setInitDate}")
            HouseOwnerInfo(text = "${installDevice?.consumeHouseNm}")
            HouseNumberInfo(text = "${installDevice?.consumeHouseNo}")
            AddressInfo(text = "${installDevice?.AreaBig} ${installDevice?.setAreaAddr}")
            HouseCheckListInfo(text = "${installDevice?.setPlaceDesc} ${installDevice?.accountCheckNote}")
            LatitudeInfo(text = "${installDevice?.gpsLatitude}")
            LongitudeInfo(text = "${installDevice?.gpsLongitude}")
        }
    }
}

@Composable
fun HouseNumberInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun AddressInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun HouseOwnerInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun HouseCheckListInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun SetDateInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun TerminalInfo(installDevice: InstallDevice?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Paddings.medium)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundGray0)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Paddings.medium)
        ) {
            Text(
                text = "TerminalInfo",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            SerialNumberInfo(text = "${installDevice?.meterDeviceSn}")
            ImeiInfo(text = "${installDevice?.nwk}")
            CtnInfo(text = "${installDevice?.cdmaNo}")
            FirmwareVersionInfo(text = "${installDevice?.firmware}")
            ServerIpPortInfo(text = "${installDevice?.serverAddr1}:${installDevice?.serverPort1}")
        }
    }

}

@Composable
fun SerialNumberInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun ImeiInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun CtnInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun FirmwareVersionInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun ServerIpPortInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun LatitudeInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun LongitudeInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun MeterInfo(installDevice: InstallDevice?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Paddings.medium)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundGray0)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Paddings.medium)
        ) {
            Text(
                text = "MeterInfo",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            MeterSerialNumberInfo(text = "${installDevice?.meterSn1}")
            MeterCaliberInfo(text = "${installDevice?.metercaliberCd1}mm")
            MeterCompanyInfo(text = "${installDevice?.meterCompany1}")
        }
    }

}

@Composable
fun MeterSerialNumberInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun MeterCaliberInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun MeterCompanyInfo(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun DeviceDetailFooter(modifier: Modifier = Modifier) {
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
            text = stringResource(id = R.string.update),
            leadingIcon = LeadingIcon(Icons.Filled.Refresh),
            onClick = {})
        PrimaryButton(
            modifier = Modifier
                .weight(1f)
                .padding(Paddings.xsmall),
            text = stringResource(id = R.string.upload),
            leadingIcon = LeadingIcon(Icons.Filled.Upload),
            onClick = {})
    }
}


@Preview(showBackground = true)
@Composable
fun DeviceDetailScreenPreview() {
    RenewSmartSetTheme {
        Surface {
            DeviceDetailScreen(installDevice = null, imageList = emptyList())
        }
    }
}