package com.hitec.presentation.main.photo_upload

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PhotoUploadScreen(
    navController: NavHostController,
    viewModel: PhotoUploadViewModel = hiltViewModel()
) {
    InitScreen(navController = navController, viewModel = viewModel)

    val state by viewModel.container.stateFlow.collectAsState()

    var currentImagePage by remember { mutableIntStateOf(0) }
    var fullSizePagerVisible by remember { mutableStateOf(false) }

    val deviceImagePagerState = rememberPagerState(
        initialPage = currentImagePage,       // 처음 페이지를 currentImagePage로
        pageCount = { state.deviceImageList.size }
    )
    val fullSizePagerState = rememberPagerState(
        initialPage = currentImagePage,
        pageCount = { state.deviceImageList.size }
    )

    LaunchedEffect(deviceImagePagerState) {
        snapshotFlow { deviceImagePagerState.currentPage }
            .collect { page ->
                currentImagePage = page
            }
    }

    LaunchedEffect(fullSizePagerState) {
        snapshotFlow { fullSizePagerState.currentPage }
            .collect { page ->
                currentImagePage = page
            }
    }

    LaunchedEffect(currentImagePage) {
        // device 쪽 Pager와 동기화
        if (deviceImagePagerState.currentPage != currentImagePage) {
            deviceImagePagerState.scrollToPage(currentImagePage)
        }
        // full size 쪽 Pager와 동기화
        if (fullSizePagerState.currentPage != currentImagePage) {
            fullSizePagerState.scrollToPage(currentImagePage)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PhotoUploadScreen(
            imageList = state.deviceImageList.sortedBy { it.first },
            devicePagerState = deviceImagePagerState,
            onPagerClick = { page ->
                currentImagePage = page
                fullSizePagerVisible = true
            }
        )
    }

    FullSizePager(
        isVisible = fullSizePagerVisible,
        imageList = state.deviceImageList.sortedBy { it.first },
        pagerState = fullSizePagerState,
        onDismiss = { currentPage ->
            currentImagePage = currentPage
            fullSizePagerVisible = false
        }
    )
}

@Composable
private fun InitScreen(
    navController: NavHostController,
    viewModel: PhotoUploadViewModel
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val deviceImei = navBackStackEntry?.arguments?.getString(ArgumentName.ARGU_DEVICE_IMEI)

    LaunchedEffect(deviceImei) {
        deviceImei?.let {
            viewModel.initialize(deviceImei)
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PhotoUploadSideEffect.Toast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
private fun PhotoUploadScreen(
    imageList: List<Pair<Int, Any?>>,
    devicePagerState: PagerState,
    onPagerClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Paddings.medium)
    ) {
        DeviceImagePager(
            imageList = imageList,
            pagerState = devicePagerState,
            onPagerClick = onPagerClick,
        )

        // 사진 테이블 UI
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                TableHeader() // 테이블 헤더
            }
            items(imageList) { (photoTypeCd, image) ->
                TableRowItem(
                    photoType = photoTypeCd,
                    image = image,
                    onPreview = { /* 클릭 시 미리보기 처리 */ },
                    onCapture = { /* 클릭 시 캡처 처리 */ }
                )
            }
        }

        // 업로드 버튼
        UploadButton(onUpload = { /* 업로드 처리 */ })
    }
}

@Composable
fun TableHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "항목",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Before",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "After",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TableRowItem(
    photoType: Int,
    image: Any?,
    onPreview: (Any?) -> Unit,
    onCapture: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray)
    ) {
        Text(
            text = "항목 $photoType",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        CaptureButton(onClick = { onCapture("Before_$photoType") })
        CaptureButton(onClick = { onCapture("After_$photoType") })
        Button(onClick = { onPreview(image) }, modifier = Modifier.padding(horizontal = 4.dp)) {
            Text("미리보기")
        }
    }
}

@Composable
fun CaptureButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
    ) {
        Text("📷")
    }
}

@Composable
fun UploadButton(onUpload: () -> Unit) {
    Button(
        onClick = onUpload,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("사진 업로드")
    }
}

@Preview
@Composable
fun PhotoUploadScreenPreview() {
    RenewSmartSetTheme {
        PhotoUploadScreen(
            imageList = emptyList(),
            devicePagerState = rememberPagerState(pageCount = { 0 }),
            onPagerClick = { },
        )
    }
}