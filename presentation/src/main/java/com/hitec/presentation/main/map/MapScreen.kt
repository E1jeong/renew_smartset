package com.hitec.presentation.main.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.hitec.domain.model.AsDevice
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.main.MainViewModel

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val navBackStackEntry = navController.previousBackStackEntry // 이전 화면 정보 가져오기
    val previousRoute = navBackStackEntry?.destination?.route

// 이전 화면에 따라 데이터 선택
    val markers = when (previousRoute) {
        "InstallDeviceScreen" -> {
            state.installDeviceList.toInstallDeviceClusterItems()
        }

        "AsDeviceScreen" -> {
            state.asDeviceList.toAsDeviceClusterItems()
        }

        else -> {
            emptyList() // 기본값
        }
    }

    val averageLatLng = if (markers.isNotEmpty()) {
        markers.calculateAverageLatLng()
    } else {
        LatLng(0.0, 0.0) // 기본값
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(averageLatLng, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        // 지도의 기본 설정입니다.
        properties = MapProperties(),
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        // Clustering 유틸리티를 사용해 마커를 클러스터링합니다.
        Clustering(
            items = markers,
            // 클러스터를 클릭했을 때 이벤트를 처리합니다.
            onClusterClick = { cluster ->
                println("클릭한 클러스터: ${cluster.items.size} 개의 항목")
                false // true로 설정하면 기본 이벤트를 막습니다.
            },
            // 개별 항목을 클릭했을 때 이벤트를 처리합니다.
            onClusterItemClick = { item ->
                println("클릭한 항목: ${item.itemTitle}")
                false
            },
            // 클러스터 커스터마이징: 항목 수를 보여줍니다.
//            clusterContent = { cluster ->
//                Text(text = "${cluster.items.size}개")
//            },
        )

//        installDeviceList.forEach { device ->
//            Marker(
//                state = rememberMarkerState(position = LatLng(device.gpsLatitude!!.toDouble(), device.gpsLongitude!!.toDouble())),
//                title = device.consumeHouseNm, // 마커 제목
//                snippet = device.setAreaAddr // 마커 설명
//            )
//        }
    }
}

data class MyClusterItem(
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String
) : ClusterItem {
    override fun getPosition(): LatLng = itemPosition
    override fun getTitle(): String = itemTitle
    override fun getSnippet(): String = itemSnippet
    override fun getZIndex(): Float? = null
}

fun List<InstallDevice>.toInstallDeviceClusterItems(): List<MyClusterItem> {
    return this.map { installDevice ->
        MyClusterItem(
            itemPosition = LatLng(
                installDevice.gpsLatitude!!.trim().toDouble(),
                installDevice.gpsLongitude!!.trim().toDouble()
            ),
            itemTitle = installDevice.consumeHouseNm ?: "", // InstallDevice 객체의 이름
            itemSnippet = installDevice.setAreaAddr ?: "" // 추가 정보
        )
    }
}

fun List<AsDevice>.toAsDeviceClusterItems(): List<MyClusterItem> {
    return this.map { asDevice ->
        MyClusterItem(
            itemPosition = LatLng(asDevice.gpsLatitude!!.trim().toDouble(), asDevice.gpsLongitude!!.trim().toDouble()),
            itemTitle = asDevice.consumeHouseNm ?: "",
            itemSnippet = asDevice.setAreaAddr ?: ""
        )
    }
}

fun List<MyClusterItem>.calculateAverageLatLng(): LatLng {
    if (this.isEmpty()) return LatLng(0.0, 0.0) // 리스트가 비어있을 경우 기본값 반환

    val avgLatitude = this.map { it.itemPosition.latitude }.average()
    val avgLongitude = this.map { it.itemPosition.longitude }.average()
    return LatLng(avgLatitude, avgLongitude)
}