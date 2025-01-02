package com.hitec.presentation.main.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Polyline
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
    val context = LocalContext.current
    val state by viewModel.container.stateFlow.collectAsState()
    val navBackStackEntry = navController.previousBackStackEntry // 이전 화면 정보 가져오기
    val previousRoute = navBackStackEntry?.destination?.route

    var currentLocationLatLng by remember { mutableStateOf<LatLng?>(null) }
    var selectedMarkerLatLng by remember { mutableStateOf<LatLng?>(null) }
    var distance by remember { mutableStateOf<Double?>(null) }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@LaunchedEffect
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocationLatLng = LatLng(location.latitude, location.longitude)
            }
        }
    }

    // 이전 화면에 따라 데이터 선택
    val markers = when (previousRoute) {
        "InstallDeviceScreen" -> state.installDeviceList.toInstallDeviceClusterItems()
        "AsDeviceScreen" -> state.asDeviceList.toAsDeviceClusterItems()
        else -> emptyList() // 기본값
    }

    val averageLatLng = if (markers.isNotEmpty()) {
        markers.calculateAverageLatLng()
    } else {
        LatLng(0.0, 0.0) // 기본값
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(averageLatLng, 10f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            // 지도의 기본 설정입니다.
            properties = MapProperties(),
            uiSettings = MapUiSettings(zoomControlsEnabled = true),
            onMapClick = {
                selectedMarkerLatLng = null
                distance = null
            }
        ) {
            // MapEffect를 통해 실제 GoogleMap 객체에 접근
            MapEffect { map ->
                // 위치 사용권한이 허용된 상태라면 아래 속성을 true로 설정
                map.isMyLocationEnabled = true
            }

            // Clustering 유틸리티를 사용해 마커를 클러스터링합니다.
            Clustering(
                items = markers,
                // 클러스터를 클릭했을 때 이벤트를 처리합니다.
                onClusterClick = { cluster ->
                    selectedMarkerLatLng = cluster.position
                    println("클릭한 클러스터: ${cluster.items.size} 개의 항목")
                    false // true로 설정하면 기본 이벤트를 막습니다.
                },
                // 개별 항목을 클릭했을 때 이벤트를 처리합니다.
                onClusterItemClick = { item ->
                    selectedMarkerLatLng = item.itemPosition
                    println("클릭한 항목: ${item.itemTitle}")
                    false
                },
            )

            // 마커와 현재 위치 사이 선 그리기
            if (currentLocationLatLng != null && selectedMarkerLatLng != null) {
                val start = currentLocationLatLng
                val end = selectedMarkerLatLng
                if (start != null && end != null) {
                    // start와 end는 이 블록 안에서는 LatLng 타입으로 스마트 캐스팅됨
                    Polyline(points = listOf(start, end))
                }

                distance = SphericalUtil.computeDistanceBetween(currentLocationLatLng, selectedMarkerLatLng)
            }
        }

        distance?.let { dist ->
            val textDistance = if (dist >= 1000) {
                "%.2f".format(dist / 1000) + "km"
            } else {
                "%.2f".format(dist) + "m"
            }

            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = "거리: $textDistance"
            )
        }
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