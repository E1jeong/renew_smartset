package com.hitec.presentation.main.device_detail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.component.button.PrimaryButton
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme
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

    DeviceDetailScreen(state.installDevice)
}

@Composable
private fun DeviceDetailScreen(installDevice: InstallDevice?) {
    val images = listOf(
        "https://picsum.photos/200/300",
        "https://via.placeholder.com/150",
        "https://dummyimage.com/300x200/000/fff",
        "https://loremflickr.com/320/240",
        "https://source.unsplash.com/random/300x200"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            ImageSliderWithIndicator(images)
            Spacer(modifier = Modifier.height(16.dp))
            HouseNumberInfo(text = "${installDevice?.consumeHouseNo}")
            AddressInfo(text = "${installDevice?.AreaBig} ${installDevice?.setAreaAddr}")
            HouseOwnerInfo(text = "${installDevice?.consumeHouseNm}")
        }
        DeviceDetailFooter()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSliderWithIndicator(images: List<String>) {
    val pagerState = rememberPagerState(pageCount = { images.size })

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

        // Indicator
        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(images.size) { iteration ->
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
fun HouseNumberInfo(text: String) {
    Row(
        modifier = Modifier.padding(start = Paddings.medium),
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
        modifier = Modifier.padding(start = Paddings.medium),
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
        modifier = Modifier.padding(start = Paddings.medium),
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
fun BoxScope.DeviceDetailFooter() {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(Paddings.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        PrimaryButton(
            modifier = Modifier
                .weight(1f)
                .padding(Paddings.xsmall),
            text = "Update",
            leadingIcon = LeadingIcon(Icons.Filled.Refresh),
            onClick = {})
        PrimaryButton(
            modifier = Modifier
                .weight(1f)
                .padding(Paddings.xsmall),
            text = "Upload",
            leadingIcon = LeadingIcon(Icons.Filled.Upload),
            onClick = {})
    }
}


@Preview
@Composable
fun DeviceDetailScreenPreview() {
    RenewSmartSetTheme {
        Surface {
            DeviceDetailScreen(installDevice = null)
        }
    }
}