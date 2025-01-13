package com.hitec.presentation.main.photo_upload

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hitec.presentation.navigation.ArgumentName
import com.hitec.presentation.theme.RenewSmartSetTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PhotoUploadScreen(
    navController: NavHostController,
    viewModel: PhotoUploadViewModel = hiltViewModel()
) {
    InitScreen(navController = navController, viewModel = viewModel)

    PhotoUploadScreen()
}

@Composable
private fun InitScreen(
    navController: NavHostController,
    viewModel: PhotoUploadViewModel
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val deviceImei = navBackStackEntry?.arguments?.getString(ArgumentName.ARGU_DEVICE_IMEI)

//    LaunchedEffect(deviceImei) {
//        deviceImei?.let {
//        }
//    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PhotoUploadSideEffect.Toast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
private fun PhotoUploadScreen() {
    Text("Photo Upload Screen")
}

@Preview
@Composable
fun PhotoUploadScreenPreview() {
    RenewSmartSetTheme {
        PhotoUploadScreen()
    }
}