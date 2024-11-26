package com.hitec.presentation.main.asdevice

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.hitec.domain.model.AsDevice
import com.hitec.presentation.R
import com.hitec.presentation.main.MainSideEffect
import com.hitec.presentation.main.MainViewModel
import com.hitec.presentation.main.asdevice.component.AsDeviceCard
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AsDeviceScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainSideEffect.Toast -> {
                Toast.makeText(
                    context,
                    sideEffect.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isNetworkLoading) {
            Text(stringResource(id = R.string.loading))
        } else {
            AsDeviceScreen(
                asDeviceList = state.asDeviceList,
                onItemClick = { }
            )
        }
    }
}

@Composable
private fun AsDeviceScreen(
    asDeviceList: List<AsDevice>,
    onItemClick: (AsDevice) -> Unit
) {
    LazyColumn {
        items(asDeviceList.size) { index ->
            AsDeviceCard(
                asDevice = asDeviceList[index],
                onClick = { onItemClick(asDeviceList[index]) })
        }
    }
}