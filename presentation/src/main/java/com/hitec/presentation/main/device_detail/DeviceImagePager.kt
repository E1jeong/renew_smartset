package com.hitec.presentation.main.device_detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hitec.presentation.R
import com.hitec.presentation.theme.Paddings

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceImagePager(imageList: List<Pair<Int, Any?>>) {
    val pagerState = rememberPagerState(pageCount = { imageList.size })
    val imagesType = imageList.map { it.first }
    val images = imageList.map { it.second }

    if (imageList.isEmpty()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Paddings.large),
            text = stringResource(id = R.string.no_image_available),
            textAlign = TextAlign.Center,
        )
    } else {
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
                    contentDescription = "pager image $page",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            //Image type (title)
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
                    val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
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
}