package com.hitec.presentation.main.camera

import android.Manifest
import android.os.Build
import android.util.Size
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.hitec.presentation.R
import com.hitec.presentation.main.MainSideEffect
import com.hitec.presentation.main.MainViewModel
import com.hitec.presentation.util.PermissionState
import com.hitec.presentation.util.permissionRequest
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun CameraScreen(sharedViewModel: MainViewModel) {
    val state = sharedViewModel.collectAsState().value
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var qrCodeValueDialogVisible by remember { mutableStateOf(false) }

    sharedViewModel.collectSideEffect { sideEffect ->
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

    val requiredPermissions = listOf(Manifest.permission.CAMERA)
    val permissionState = permissionRequest(
        permissions = requiredPermissions,
        rationaleTitle = stringResource(id = R.string.camera_dialog_rationale_title),
        rationaleText = stringResource(id = R.string.camera_dialog_rationale_text)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        when (permissionState) {
            PermissionState.Granted -> {
                CameraScreen(
                    lifecycleOwner = lifecycleOwner,
                    qrCodeValue = state.qrCodeValue,
                    onQrCodeValueChange = sharedViewModel::onQrCodeValueChange,
                    onQrCodeValueDetect = { qrCodeValueDialogVisible = true }
                )
            }

            PermissionState.Denied, PermissionState.NeedsSpecialPermission -> {
                Text(text = stringResource(id = R.string.camera_dialog_rationale_text))
            }
        }
    }

    QrCodeValueDialog(
        visible = qrCodeValueDialogVisible,
        qrCodeValue = state.qrCodeValue,
        onDismissRequest = { qrCodeValueDialogVisible = false }
    )
}


@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
private fun CameraScreen(
    lifecycleOwner: LifecycleOwner,
    qrCodeValue: String,
    onQrCodeValueChange: (String) -> Unit,
    onQrCodeValueDetect: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(previewView.width, previewView.height))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                            processImageProxy(imageProxy, onQrCodeValueChange, onQrCodeValueDetect)
                        }
                    }

                try {
                    ProcessCameraProvider.getInstance(context).get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                previewView
            },
        )
        QrCodeValueText(qrCodeValue = qrCodeValue)
    }
}

@androidx.camera.core.ExperimentalGetImage
private fun processImageProxy(
    imageProxy: ImageProxy,
    onQrCodeValueChange: (String) -> Unit,
    onQrCodeValueDetect: () -> Unit
) {
    imageProxy.image?.let { image ->
        val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { qrValue ->
                        onQrCodeValueChange(qrValue)
                        onQrCodeValueDetect()
                    }
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}

@Composable
private fun QrCodeValueText(qrCodeValue: String) {
    Text(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        text = qrCodeValue,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge
    )
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraScreenPreview() {
    Surface {
        //CameraScreen()
    }
}
