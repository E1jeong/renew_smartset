package com.hitec.presentation.main.photo_upload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.hitec.presentation.theme.Paddings

@Composable
fun FullSizePager(
    isVisible: Boolean = false,
    imageList: List<Pair<Int, Any?>>,
    pagerState: PagerState,
    onDismiss: (Int) -> Unit
) {
    val imagesType = imageList.map { it.first }
    val images = imageList.map { it.second }

    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Full screen pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                images[page]?.let { image ->
                    AsyncImage(
                        model = image,
                        contentDescription = "Full Screen Image $page",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Image title
            if (pagerState.currentPage in imageList.indices) {
                Text(
                    text = ImageManager.parsePhotoTypeCd(imagesType[pagerState.currentPage]),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = Paddings.xextra),
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
            }

            // Close button
            IconButton(
                onClick = { onDismiss(pagerState.currentPage) },
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(Paddings.large)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }
    }
}